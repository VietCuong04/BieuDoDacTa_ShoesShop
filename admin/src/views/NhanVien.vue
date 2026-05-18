<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-people me-2"></i>Quản lý Nhân viên</h4>
        <p class="text-muted mb-0 small">{{ items.length }} nhân viên</p>
      </div>
      <button class="btn btn-primary" @click="openAdd" v-if="isAdmin">
        <i class="bi bi-plus-lg me-1"></i> Thêm nhân viên
      </button>
    </div>

    <div v-if="!isAdmin" class="alert alert-info d-flex align-items-center gap-2 mb-4">
      <i class="bi bi-info-circle"></i>
      Bạn đang xem danh sách nhân viên. Chỉ Admin mới có quyền thêm/sửa/xóa.
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3" style="width:50px">#</th>
                <th>Tên nhân viên</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Giới tính</th>
                <th>Vai trò</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th class="text-center" style="width:120px" v-if="isAdmin">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="items.length === 0">
                <td :colspan="isAdmin ? 9 : 8" class="text-center text-muted py-4">Không có dữ liệu</td>
              </tr>
              <tr v-for="(item, idx) in items" :key="item.maTaiKhoan">
                <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                <td>
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-circle" :class="item.loaiTaiKhoan === 'ADMIN' ? 'bg-danger' : 'bg-primary'">
                      {{ (item.tenNguoiDung || 'N')[0].toUpperCase() }}
                    </div>
                    <div class="fw-semibold">{{ item.tenNguoiDung || '—' }}</div>
                  </div>
                </td>
                <td class="text-muted">{{ item.email || '—' }}</td>
                <td>{{ item.soDienThoai || '—' }}</td>
                <td>{{ item.gioiTinh || '—' }}</td>
                <td>
                  <span :class="item.loaiTaiKhoan === 'ADMIN' ? 'badge bg-danger' : 'badge bg-primary'">
                    {{ item.loaiTaiKhoan === 'ADMIN' ? 'Admin' : 'Nhân viên' }}
                  </span>
                </td>
                <td>
                  <span :class="item.trangThai === 'ACTIVE' ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                    {{ item.trangThai === 'ACTIVE' ? 'Hoạt động' : 'Khóa' }}
                  </span>
                </td>
                <td class="text-muted small">{{ formatDate(item.ngayKhoiTao) }}</td>
                <td class="text-center" v-if="isAdmin">
                  <button class="btn btn-sm btn-outline-primary me-1" @click="openEdit(item)" title="Sửa">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="remove(item.maTaiKhoan)" title="Xóa"
                    :disabled="item.maTaiKhoan === user?.maTaiKhoan">
                    <i class="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade show d-block" v-if="showModal" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title fw-bold">{{ isEditMode ? 'Sửa' : 'Thêm' }} Nhân viên</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <form @submit.prevent="save">
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label fw-semibold">Tên nhân viên</label>
                <input v-model="form.tenNguoiDung" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Email</label>
                <input v-model="form.email" type="email" class="form-control" required />
              </div>
              <div class="mb-3" v-if="!isEditMode">
                <label class="form-label fw-semibold">Mật khẩu</label>
                <input v-model="form.matKhau" type="password" class="form-control" required />
              </div>
              <div class="mb-3" v-if="isEditMode">
                <label class="form-label fw-semibold">Đổi mật khẩu <span class="text-muted fw-normal">(để trống nếu không đổi)</span></label>
                <input v-model="form.matKhau" type="password" class="form-control" />
              </div>
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">SĐT</label>
                  <input v-model="form.soDienThoai" class="form-control" />
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Giới tính</label>
                  <select v-model="form.gioiTinh" class="form-select">
                    <option value="">-- Chọn --</option>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                    <option value="Khác">Khác</option>
                  </select>
                </div>
              </div>
              <div class="row g-3 mt-0">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Vai trò</label>
                  <select v-model="form.loaiTaiKhoan" class="form-select">
                    <option value="NHANVIEN">Nhân viên</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Trạng thái</label>
                  <select v-model="form.trangThai" class="form-select">
                    <option value="ACTIVE">Hoạt động</option>
                    <option value="INACTIVE">Khóa</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showModal = false">Hủy</button>
              <button type="submit" class="btn btn-primary">{{ isEditMode ? 'Cập nhật' : 'Lưu' }}</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const API = 'http://localhost:8080/api'
const items = ref([])
const showModal = ref(false)
const isEditMode = ref(false)
const editId = ref(null)
const form = ref({
  tenNguoiDung: '', email: '', matKhau: '', soDienThoai: '',
  gioiTinh: '', loaiTaiKhoan: 'NHANVIEN', trangThai: 'ACTIVE'
})

const user = computed(() => JSON.parse(localStorage.getItem('admin_user') || 'null'))
const isAdmin = computed(() => user.value?.loaiTaiKhoan === 'ADMIN')
const headers = computed(() => {
  const h = { 'Content-Type': 'application/json' }
  if (user.value) h['X-User-Id'] = String(user.value.maTaiKhoan)
  return h
})

function formatDate(d) { return d ? new Date(d).toLocaleDateString('vi-VN') : '' }

async function fetchData() {
  try {
    const res = await fetch(`${API}/employees`, { headers: headers.value })
    if (res.ok) {
      items.value = await res.json()
    } else {
      const err = await res.json().catch(() => null)
      console.error('Lỗi tải nhân viên:', err?.error || res.statusText)
    }
  } catch (e) { console.error(e) }
}

function openAdd() {
  isEditMode.value = false; editId.value = null
  form.value = { tenNguoiDung: '', email: '', matKhau: '', soDienThoai: '', gioiTinh: '', loaiTaiKhoan: 'NHANVIEN', trangThai: 'ACTIVE' }
  showModal.value = true
}

function openEdit(item) {
  isEditMode.value = true; editId.value = item.maTaiKhoan
  form.value = {
    tenNguoiDung: item.tenNguoiDung || '',
    email: item.email || '',
    matKhau: '',
    soDienThoai: item.soDienThoai || '',
    gioiTinh: item.gioiTinh || '',
    loaiTaiKhoan: item.loaiTaiKhoan || 'NHANVIEN',
    trangThai: item.trangThai || 'ACTIVE'
  }
  showModal.value = true
}

async function save() {
  try {
    const body = { ...form.value }
    if (isEditMode.value && !body.matKhau) {
      delete body.matKhau
    }
    const url = isEditMode.value ? `${API}/employees/${editId.value}` : `${API}/employees`
    const method = isEditMode.value ? 'PUT' : 'POST'
    const res = await fetch(url, { method, headers: headers.value, body: JSON.stringify(body) })
    if (!res.ok) {
      const err = await res.json().catch(() => null)
      throw new Error(err?.error || 'Thao tác thất bại')
    }
    showModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function remove(id) {
  if (!confirm('Bạn có chắc muốn xóa nhân viên này?')) return
  try {
    const res = await fetch(`${API}/employees/${id}`, { method: 'DELETE', headers: headers.value })
    if (!res.ok) {
      const err = await res.json().catch(() => null)
      throw new Error(err?.error || 'Xóa thất bại')
    }
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

onMounted(fetchData)
</script>

<style scoped>
.avatar-circle {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 700;
  font-size: 0.82rem;
  flex-shrink: 0;
}
</style>
