<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-globe-americas me-2"></i>Quản lý Xuất xứ</h4>
        <p class="text-muted mb-0 small">{{ items.length }} xuất xứ</p>
      </div>
      <button class="btn btn-primary" @click="openAdd">
        <i class="bi bi-plus-lg me-1"></i> Thêm mới
      </button>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3" style="width:50px">#</th>
                <th>Tên xuất xứ</th>
                <th>Trạng thái</th>
                <th>Người khởi tạo</th>
                <th>Ngày khởi tạo</th>
                <th style="width:120px" class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="items.length === 0">
                <td colspan="6" class="text-center text-muted py-4">Không có dữ liệu</td>
              </tr>
              <tr v-for="(item, idx) in items" :key="item.maXuatXu">
                <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                <td class="fw-semibold">{{ item.tenXuatXu }}</td>
                <td>
                  <span :class="item.trangThai === 1 ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                    {{ item.trangThai === 1 ? 'Hoạt động' : 'Ngừng' }}
                  </span>
                </td>
                <td class="text-muted">{{ item.nguoiKhoiTao }}</td>
                <td class="text-muted small">{{ formatDate(item.ngayKhoiTao) }}</td>
                <td class="text-center">
                  <button class="btn btn-sm btn-outline-primary me-1" @click="openEdit(item)" title="Sửa">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="remove(item.maXuatXu)" title="Xóa">
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
            <h5 class="modal-title fw-bold">{{ isEdit ? 'Sửa' : 'Thêm' }} Xuất xứ</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <form @submit.prevent="save">
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label fw-semibold">Tên xuất xứ</label>
                <input v-model="form.tenXuatXu" class="form-control" required placeholder="Nhập tên xuất xứ" />
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Trạng thái</label>
                <select v-model="form.trangThai" class="form-select">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showModal = false">Hủy</button>
              <button type="submit" class="btn btn-primary">{{ isEdit ? 'Cập nhật' : 'Lưu' }}</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const API = 'http://localhost:8080/api/origins'
const items = ref([])
const showModal = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = ref({ tenXuatXu: '', trangThai: 1 })

function formatDate(d) { return d ? new Date(d).toLocaleDateString('vi-VN') : '' }

async function fetchData() {
  try { items.value = await fetch(API).then(r => r.json()) } catch (e) { console.error(e) }
}

function openAdd() {
  isEdit.value = false; editId.value = null
  form.value = { tenXuatXu: '', trangThai: 1 }
  showModal.value = true
}

function openEdit(item) {
  isEdit.value = true; editId.value = item.maXuatXu
  form.value = { tenXuatXu: item.tenXuatXu, trangThai: item.trangThai }
  showModal.value = true
}

async function save() {
  try {
    const user = JSON.parse(localStorage.getItem('admin_user') || '{}')
    const body = { ...form.value, nguoiKhoiTao: user.tenNguoiDung || 'admin', nguoiCapNhat: user.tenNguoiDung || 'admin' }
    if (isEdit.value) {
      await fetch(`${API}/${editId.value}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    } else {
      await fetch(API, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    }
    showModal.value = false; fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function remove(id) {
  if (!confirm('Bạn có chắc muốn xóa?')) return
  try { await fetch(`${API}/${id}`, { method: 'DELETE' }); fetchData() } catch (e) { alert('Lỗi: ' + e.message) }
}

onMounted(fetchData)
</script>
