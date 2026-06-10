<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-ticket-perforated me-2"></i>Quản lý Phiếu giảm giá</h4>
        <p class="text-muted mb-0 small">{{ items.length }} phiếu giảm giá</p>
      </div>
      <button class="btn btn-primary" @click="openAdd">
        <i class="bi bi-plus-lg me-1"></i> Thêm mới
      </button>
    </div>

    <!-- Status filter tabs -->
    <ul class="nav nav-tabs mb-3">
      <li class="nav-item">
        <button class="nav-link" :class="{ active: filterStatus === '' }" @click="filterStatus = ''">Tất cả</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" :class="{ active: filterStatus === 1 }" @click="filterStatus = 1">
          <i class="bi bi-check-circle me-1"></i>Đang hoạt động
        </button>
      </li>
      <li class="nav-item">
        <button class="nav-link" :class="{ active: filterStatus === 0 }" @click="filterStatus = 0">
          <i class="bi bi-x-circle me-1"></i>Ngừng
        </button>
      </li>
    </ul>

    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3" style="width:50px">#</th>
                <th>Tên / Mã code</th>
                <th>Loại</th>
                <th>Mức giảm</th>
                <th>Giảm tối đa</th>
                <th>Số lượng</th>
                <th>ĐK áp dụng</th>
                <th>Thời gian</th>
                <th>Trạng thái</th>
                <th style="width:120px" class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="filteredItems.length === 0">
                <td colspan="10" class="text-center text-muted py-4">Không có dữ liệu</td>
              </tr>
              <tr v-for="(item, idx) in filteredItems" :key="item.maGiamGia">
                <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                <td>
                  <div class="fw-semibold">{{ item.tenGiamGia }}</div>
                  <div v-if="item.maCode" class="mt-1">
                    <span class="badge bg-dark font-monospace" style="letter-spacing:1px;font-size:0.75rem;">
                      <i class="bi bi-tag me-1"></i>{{ item.maCode }}
                    </span>
                  </div>
                  <div class="text-muted small text-truncate" style="max-width:200px" v-if="item.moTa">{{ item.moTa }}</div>
                </td>
                <td>
                  <span :class="item.loaiGiamGia === 1 ? 'badge bg-info-subtle text-info' : 'badge bg-warning-subtle text-warning'">
                    {{ item.loaiGiamGia === 1 ? 'Phần trăm' : 'Số tiền' }}
                  </span>
                </td>
                <td class="fw-semibold text-danger">
                  {{ item.loaiGiamGia === 1 ? item.mucGiamGia + '%' : formatPrice(item.mucGiamGia) }}
                </td>
                <td>{{ item.giamToiDa ? formatPrice(item.giamToiDa) : '—' }}</td>
                <td>
                  <span class="badge bg-secondary-subtle text-secondary">{{ item.soLuong ?? '—' }}</span>
                </td>
                <td>{{ item.dieuKienApDung ? formatPrice(item.dieuKienApDung) : '—' }}</td>
                <td class="small">
                  <div>{{ formatDate(item.ngayBatDau) }}</div>
                  <div class="text-muted">→ {{ formatDate(item.ngayKetThuc) }}</div>
                </td>
                <td>
                  <span :class="item.trangThai === 1 ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                    {{ item.trangThai === 1 ? 'Hoạt động' : 'Ngừng' }}
                  </span>
                </td>
                <td class="text-center">
                  <button class="btn btn-sm btn-outline-primary me-1" @click="openEdit(item)" title="Sửa">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="remove(item.maGiamGia)" title="Xóa">
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
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title fw-bold">{{ isEdit ? 'Sửa' : 'Thêm' }} Phiếu giảm giá</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <form @submit.prevent="save">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Tên giảm giá</label>
                  <input v-model="form.tenGiamGia" class="form-control" required placeholder="VD: Giảm 20% đơn từ 500K" />
                </div>
                <div class="col-md-3">
                  <label class="form-label fw-semibold">
                    Mã code
                    <span class="text-danger">*</span>
                    <span class="text-muted fw-normal small ms-1">(khách hàng nhập)</span>
                  </label>
                  <input v-model="form.maCode" class="form-control font-monospace text-uppercase" required
                    placeholder="VD: SUMMER20" maxlength="30"
                    style="letter-spacing:1px"
                    @input="form.maCode = form.maCode.toUpperCase()" />
                </div>
                <div class="col-md-3">
                  <label class="form-label fw-semibold">Loại giảm giá</label>
                  <select v-model="form.loaiGiamGia" class="form-select" required>
                    <option :value="1">Phần trăm (%)</option>
                    <option :value="2">Số tiền (VNĐ)</option>
                  </select>
                </div>
              </div>
              <div class="mb-3 mt-3">
                <label class="form-label fw-semibold">Mô tả</label>
                <textarea v-model="form.moTa" class="form-control" rows="2" placeholder="Mô tả chi tiết..."></textarea>
              </div>
              <div class="row g-3">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Mức giảm giá</label>
                  <input v-model="form.mucGiamGia" class="form-control" required :placeholder="form.loaiGiamGia === 1 ? 'VD: 20' : 'VD: 50000'" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Giảm tối đa (VNĐ)</label>
                  <input v-model.number="form.giamToiDa" type="number" class="form-control" min="0" step="1000" placeholder="0" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Số lượng</label>
                  <input v-model.number="form.soLuong" type="number" class="form-control" min="0" required placeholder="0" />
                </div>
              </div>
              <div class="row g-3 mt-0">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">ĐK áp dụng (VNĐ)</label>
                  <input v-model.number="form.dieuKienApDung" type="number" class="form-control" min="0" step="1000" placeholder="Đơn tối thiểu" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Ngày bắt đầu</label>
                  <input v-model="form.ngayBatDau" type="datetime-local" class="form-control" required />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Ngày kết thúc</label>
                  <input v-model="form.ngayKetThuc" type="datetime-local" class="form-control" required />
                </div>
              </div>
              <div class="mt-3">
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
import { ref, computed, onMounted } from 'vue'

const API = 'https://shoes-web-be-t7xh.onrender.com/api/discounts'
const items = ref([])
const showModal = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const filterStatus = ref('')

const defaultForm = () => ({
  tenGiamGia: '',
  maCode: '',
  moTa: '',
  mucGiamGia: '',
  loaiGiamGia: 1,
  giamToiDa: null,
  soLuong: 0,
  dieuKienApDung: null,
  ngayBatDau: '',
  ngayKetThuc: '',
  trangThai: 1
})
const form = ref(defaultForm())

const filteredItems = computed(() => {
  if (filterStatus.value === '') return items.value
  return items.value.filter(i => Number(i.trangThai) === Number(filterStatus.value))
})

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('vi-VN') : '—'
}

function formatPrice(v) {
  if (v == null) return '—'
  const n = typeof v === 'string' ? parseFloat(v) : v
  if (isNaN(n)) return v
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(n)
}

function toLocalInput(dt) {
  if (!dt) return ''
  const d = new Date(dt)
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

async function fetchData() {
  try { items.value = await fetch(API).then(r => r.json()) } catch (e) { console.error(e) }
}

function openAdd() {
  isEdit.value = false
  editId.value = null
  form.value = defaultForm()
  showModal.value = true
}

function openEdit(item) {
  isEdit.value = true
  editId.value = item.maGiamGia
  form.value = {
    tenGiamGia: item.tenGiamGia || '',
    maCode: item.maCode || '',
    moTa: item.moTa || '',
    mucGiamGia: item.mucGiamGia || '',
    loaiGiamGia: item.loaiGiamGia || 1,
    giamToiDa: item.giamToiDa,
    soLuong: item.soLuong || 0,
    dieuKienApDung: item.dieuKienApDung,
    ngayBatDau: toLocalInput(item.ngayBatDau),
    ngayKetThuc: toLocalInput(item.ngayKetThuc),
    trangThai: item.trangThai
  }
  showModal.value = true
}

async function save() {
  try {
    const body = { ...form.value }
    if (isEdit.value) {
      await fetch(`${API}/${editId.value}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    } else {
      await fetch(API, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    }
    showModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function remove(id) {
  if (!confirm('Bạn có chắc muốn xóa phiếu giảm giá này?')) return
  try { await fetch(`${API}/${id}`, { method: 'DELETE' }); fetchData() } catch (e) { alert('Lỗi: ' + e.message) }
}

onMounted(fetchData)
</script>
