<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-person-lines-fill me-2"></i>Quản lý Khách hàng</h4>
        <p class="text-muted mb-0 small">{{ filtered.length }} khách hàng</p>
      </div>
    </div>

    <!-- Filters -->
    <div class="card border-0 shadow-sm mb-3">
      <div class="card-body py-2 px-3">
        <div class="row g-2 align-items-center">
          <div class="col-md-4">
            <div class="input-group input-group-sm">
              <span class="input-group-text bg-white"><i class="bi bi-search"></i></span>
              <input type="text" class="form-control" v-model="search" placeholder="Tìm tên, email, SĐT..." />
            </div>
          </div>
          <div class="col-md-3">
            <select class="form-select form-select-sm" v-model="filterStatus">
              <option value="">Tất cả trạng thái</option>
              <option value="ACTIVE">Hoạt động</option>
              <option value="LOCKED">Đã khóa</option>
            </select>
          </div>
          <div class="col-md-3">
            <select class="form-select form-select-sm" v-model="filterGender">
              <option value="">Tất cả giới tính</option>
              <option value="Nam">Nam</option>
              <option value="Nữ">Nữ</option>
            </select>
          </div>
          <div class="col-md-2 text-end">
            <button class="btn btn-sm btn-outline-secondary" @click="resetFilters">
              <i class="bi bi-arrow-counterclockwise me-1"></i>Xóa lọc
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Stats Cards -->
    <div class="row g-3 mb-3">
      <div class="col-md-3">
        <div class="card border-0 shadow-sm text-center py-3">
          <div class="fs-3 fw-bold text-primary">{{ items.length }}</div>
          <div class="small text-muted">Tổng khách hàng</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card border-0 shadow-sm text-center py-3">
          <div class="fs-3 fw-bold text-success">{{ items.filter(i => i.trangThai === 'ACTIVE').length }}</div>
          <div class="small text-muted">Đang hoạt động</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card border-0 shadow-sm text-center py-3">
          <div class="fs-3 fw-bold text-danger">{{ items.filter(i => i.trangThai === 'LOCKED').length }}</div>
          <div class="small text-muted">Đã khóa</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card border-0 shadow-sm text-center py-3">
          <div class="fs-3 fw-bold text-info">{{ newThisMonth }}</div>
          <div class="small text-muted">Mới trong tháng</div>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3" style="width:50px">#</th>
                <th>Khách hàng</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Giới tính</th>
                <th>Trạng thái</th>
                <th>Ngày đăng ký</th>
                <th class="text-center" style="width:160px" v-if="isAdmin">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="filtered.length === 0">
                <td :colspan="isAdmin ? 8 : 7" class="text-center text-muted py-4">
                  <i class="bi bi-inbox fs-3 d-block mb-2"></i>
                  Không tìm thấy khách hàng
                </td>
              </tr>
              <tr v-for="(item, idx) in filtered" :key="item.maTaiKhoan">
                <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                <td>
                  <div class="d-flex align-items-center gap-2">
                    <div class="avatar-circle bg-info">
                      {{ (item.tenNguoiDung || 'K')[0].toUpperCase() }}
                    </div>
                    <div>
                      <div class="fw-semibold">{{ item.tenNguoiDung || '—' }}</div>
                      <div class="small text-muted">ID: {{ item.maTaiKhoan }}</div>
                    </div>
                  </div>
                </td>
                <td class="text-muted small">{{ item.email || '—' }}</td>
                <td>{{ item.soDienThoai || '—' }}</td>
                <td>{{ item.gioiTinh || '—' }}</td>
                <td>
                  <span :class="item.trangThai === 'ACTIVE' ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                    <i :class="item.trangThai === 'ACTIVE' ? 'bi bi-check-circle me-1' : 'bi bi-lock me-1'"></i>
                    {{ item.trangThai === 'ACTIVE' ? 'Hoạt động' : 'Đã khóa' }}
                  </span>
                </td>
                <td class="text-muted small">{{ formatDate(item.ngayKhoiTao) }}</td>
                <td class="text-center" v-if="isAdmin">
                  <button class="btn btn-sm btn-outline-info me-1" @click="viewDetail(item)" title="Xem chi tiết">
                    <i class="bi bi-eye"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-primary me-1" @click="openEdit(item)" title="Sửa">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm" :class="item.trangThai === 'ACTIVE' ? 'btn-outline-warning' : 'btn-outline-success'" 
                    @click="toggleStatus(item)" :title="item.trangThai === 'ACTIVE' ? 'Khóa tài khoản' : 'Mở khóa'">
                    <i :class="item.trangThai === 'ACTIVE' ? 'bi bi-lock' : 'bi bi-unlock'"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Detail Modal -->
    <div class="modal fade show d-block" v-if="showDetail" tabindex="-1" style="background:rgba(0,0,0,0.5)" @click.self="showDetail=false">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-0 pb-0">
            <h5 class="modal-title fw-bold"><i class="bi bi-person-badge me-2"></i>Chi tiết khách hàng</h5>
            <button type="button" class="btn-close" @click="showDetail=false"></button>
          </div>
          <div class="modal-body" v-if="detailItem">
            <div class="text-center mb-3">
              <div class="avatar-circle-lg bg-info mx-auto mb-2">
                {{ (detailItem.tenNguoiDung || 'K')[0].toUpperCase() }}
              </div>
              <h5 class="fw-bold mb-0">{{ detailItem.tenNguoiDung || '—' }}</h5>
              <span :class="detailItem.trangThai === 'ACTIVE' ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                {{ detailItem.trangThai === 'ACTIVE' ? 'Hoạt động' : 'Đã khóa' }}
              </span>
            </div>
            <table class="table table-sm table-borderless">
              <tbody>
                <tr><td class="text-muted" style="width:140px">Mã tài khoản</td><td class="fw-semibold">{{ detailItem.maTaiKhoan }}</td></tr>
                <tr><td class="text-muted">Email</td><td>{{ detailItem.email || '—' }}</td></tr>
                <tr><td class="text-muted">Số điện thoại</td><td>{{ detailItem.soDienThoai || '—' }}</td></tr>
                <tr><td class="text-muted">Giới tính</td><td>{{ detailItem.gioiTinh || '—' }}</td></tr>
                <tr><td class="text-muted">Năm sinh</td><td>{{ detailItem.namSinh ? formatDate(detailItem.namSinh) : '—' }}</td></tr>
                <tr><td class="text-muted">Ngày đăng ký</td><td>{{ formatDate(detailItem.ngayKhoiTao) }}</td></tr>
                <tr><td class="text-muted">Đơn hàng</td><td><span class="badge bg-primary">{{ customerOrders.length }} đơn</span></td></tr>
              </tbody>
            </table>
            <!-- Customer Orders -->
            <div v-if="customerOrders.length > 0">
              <h6 class="fw-bold mt-3 mb-2"><i class="bi bi-receipt me-1"></i>Lịch sử đơn hàng</h6>
              <div class="list-group list-group-flush" style="max-height:200px;overflow-y:auto">
                <div class="list-group-item px-0 d-flex justify-content-between align-items-center" v-for="o in customerOrders" :key="o.maDonHang">
                  <div>
                    <span class="fw-semibold">#{{ o.maDonHang }}</span>
                    <span class="text-muted ms-2 small">{{ formatDate(o.ngayKhoiTao) }}</span>
                  </div>
                  <div>
                    <span class="me-2 small">{{ formatPrice(o.soTienThanhToan) }}</span>
                    <span :class="orderStatusClass(o.trangThai)" class="badge">{{ orderStatusLabel(o.trangThai) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer border-0">
            <button class="btn btn-secondary" @click="showDetail=false">Đóng</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Edit Modal -->
    <div class="modal fade show d-block" v-if="showModal" tabindex="-1" style="background:rgba(0,0,0,0.5)" @click.self="showModal=false">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-0">
            <h5 class="modal-title fw-bold"><i class="bi bi-pencil-square me-2"></i>Chỉnh sửa khách hàng</h5>
            <button type="button" class="btn-close" @click="showModal=false"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label fw-semibold">Tên khách hàng</label>
              <input type="text" class="form-control" v-model="form.tenNguoiDung" />
            </div>
            <div class="mb-3">
              <label class="form-label fw-semibold">Email</label>
              <input type="email" class="form-control" v-model="form.email" />
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-semibold">Số điện thoại</label>
                <input type="text" class="form-control" v-model="form.soDienThoai" />
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-semibold">Giới tính</label>
                <select class="form-select" v-model="form.gioiTinh">
                  <option value="">-- Chọn --</option>
                  <option value="Nam">Nam</option>
                  <option value="Nữ">Nữ</option>
                </select>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label fw-semibold">Trạng thái</label>
              <select class="form-select" v-model="form.trangThai">
                <option value="ACTIVE">Hoạt động</option>
                <option value="LOCKED">Đã khóa</option>
              </select>
            </div>
            <div class="mb-3">
              <label class="form-label fw-semibold">Mật khẩu mới <span class="text-muted fw-normal">(để trống nếu không đổi)</span></label>
              <input type="password" class="form-control" v-model="form.matKhau" placeholder="••••••" />
            </div>
          </div>
          <div class="modal-footer border-0">
            <button class="btn btn-secondary" @click="showModal=false">Hủy</button>
            <button class="btn btn-primary" @click="save" :disabled="saving">
              <span v-if="saving" class="spinner-border spinner-border-sm me-1"></span>
              Lưu thay đổi
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const API = 'https://shoes-web-be-t7xh.onrender.com/api'
const items = ref([])
const search = ref('')
const filterStatus = ref('')
const filterGender = ref('')
const showModal = ref(false)
const showDetail = ref(false)
const detailItem = ref(null)
const customerOrders = ref([])
const saving = ref(false)
const editingId = ref(null)
const form = ref({ tenNguoiDung: '', email: '', soDienThoai: '', gioiTinh: '', trangThai: 'ACTIVE', matKhau: '' })

const user = computed(() => JSON.parse(localStorage.getItem('admin_user') || 'null'))
const isAdmin = computed(() => user.value?.loaiTaiKhoan === 'ADMIN')

const filtered = computed(() => {
  let list = items.value
  if (search.value) {
    const q = search.value.toLowerCase()
    list = list.filter(i =>
      (i.tenNguoiDung || '').toLowerCase().includes(q) ||
      (i.email || '').toLowerCase().includes(q) ||
      (i.soDienThoai || '').includes(q)
    )
  }
  if (filterStatus.value) list = list.filter(i => i.trangThai === filterStatus.value)
  if (filterGender.value) list = list.filter(i => i.gioiTinh === filterGender.value)
  return list
})

const newThisMonth = computed(() => {
  const now = new Date()
  const y = now.getFullYear(), m = now.getMonth()
  return items.value.filter(i => {
    if (!i.ngayKhoiTao) return false
    const d = new Date(i.ngayKhoiTao)
    return d.getFullYear() === y && d.getMonth() === m
  }).length
})

function resetFilters() {
  search.value = ''
  filterStatus.value = ''
  filterGender.value = ''
}

function formatDate(d) {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('vi-VN')
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

function orderStatusLabel(s) {
  const m = { CHO_XAC_NHAN: 'Chờ xác nhận', DA_XAC_NHAN: 'Đã xác nhận', DANG_GIAO: 'Đang giao', HOAN_THANH: 'Hoàn thành', DA_HUY: 'Đã hủy' }
  return m[s] || s
}

function orderStatusClass(s) {
  const m = { CHO_XAC_NHAN: 'bg-warning text-dark', DA_XAC_NHAN: 'bg-info', DANG_GIAO: 'bg-primary', HOAN_THANH: 'bg-success', DA_HUY: 'bg-danger' }
  return m[s] || 'bg-secondary'
}

async function fetchData() {
  try {
    const res = await fetch(`${API}/employees/customers`, {
      headers: { 'X-User-Id': String(user.value?.maTaiKhoan || '') }
    })
    if (res.ok) items.value = await res.json()
  } catch (e) { console.error(e) }
}

async function viewDetail(item) {
  detailItem.value = item
  customerOrders.value = []
  showDetail.value = true
  try {
    const res = await fetch(`${API}/orders/customer/${item.maTaiKhoan}`)
    if (res.ok) customerOrders.value = await res.json()
  } catch (e) { console.error(e) }
}

function openEdit(item) {
  editingId.value = item.maTaiKhoan
  form.value = {
    tenNguoiDung: item.tenNguoiDung || '',
    email: item.email || '',
    soDienThoai: item.soDienThoai || '',
    gioiTinh: item.gioiTinh || '',
    trangThai: item.trangThai || 'ACTIVE',
    matKhau: '',
  }
  showModal.value = true
}

async function save() {
  saving.value = true
  try {
    const body = { ...form.value }
    if (!body.matKhau) delete body.matKhau
    const res = await fetch(`${API}/employees/customers/${editingId.value}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json', 'X-User-Id': String(user.value?.maTaiKhoan || '') },
      body: JSON.stringify(body),
    })
    if (!res.ok) {
      const err = await res.json()
      alert(err.error || 'Lỗi cập nhật')
      return
    }
    showModal.value = false
    fetchData()
  } catch (e) {
    alert('Lỗi: ' + e.message)
  } finally {
    saving.value = false
  }
}

async function toggleStatus(item) {
  const action = item.trangThai === 'ACTIVE' ? 'khóa' : 'mở khóa'
  if (!confirm(`Bạn có chắc muốn ${action} tài khoản "${item.tenNguoiDung}"?`)) return
  try {
    const res = await fetch(`${API}/employees/customers/${item.maTaiKhoan}/toggle-status`, {
      method: 'PATCH',
      headers: { 'X-User-Id': String(user.value?.maTaiKhoan || '') },
    })
    if (!res.ok) {
      const err = await res.json()
      alert(err.error || 'Lỗi')
      return
    }
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

onMounted(fetchData)
</script>

<style scoped>
.avatar-circle {
  width: 36px; height: 36px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  color: #fff; font-weight: 700; font-size: 0.85rem; flex-shrink: 0;
}
.avatar-circle-lg {
  width: 64px; height: 64px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  color: #fff; font-weight: 700; font-size: 1.5rem;
}
</style>
