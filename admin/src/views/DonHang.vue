<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-receipt me-2"></i>Quản lý Đơn hàng</h4>
        <p class="text-muted mb-0 small">{{ items.length }} đơn hàng</p>
      </div>
      <button class="btn btn-primary" @click="openAdd">
        <i class="bi bi-plus-lg me-1"></i> Tạo đơn hàng
      </button>
    </div>

    <!-- Filter tabs -->
    <div class="card border-0 shadow-sm mb-3">
      <div class="card-body py-2 px-3">
        <div class="d-flex gap-2 flex-wrap">
          <button v-for="s in statusFilters" :key="s.value"
            :class="['btn btn-sm', filterStatus === s.value ? 'btn-dark' : 'btn-outline-secondary']"
            @click="filterStatus = s.value">
            {{ s.label }}
            <span class="badge bg-white text-dark ms-1" v-if="countByStatus(s.value)">{{ countByStatus(s.value) }}</span>
          </button>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3">Mã</th>
                <th>Khách hàng</th>
                <th>SĐT</th>
                <th>Tổng tiền</th>
                <th>Giảm giá</th>
                <th>Thanh toán</th>
                <th>PTTT</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th class="text-center" style="width:130px">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="filteredItems.length === 0">
                <td colspan="10" class="text-center text-muted py-4">Không có đơn hàng</td>
              </tr>
              <tr v-for="o in filteredItems" :key="o.maDonHang">
                <td class="ps-3 fw-semibold">#{{ o.maDonHang }}</td>
                <td>
                  <div>{{ o.tenKhachHang || '—' }}</div>
                  <div class="small text-muted" v-if="o.email">{{ o.email }}</div>
                </td>
                <td class="text-muted small">{{ o.soDienThoai || '—' }}</td>
                <td>{{ formatPrice(o.soTienBanDau) }}</td>
                <td class="text-success">{{ o.soTienDuocGiam ? '-' + formatPrice(o.soTienDuocGiam) : '—' }}</td>
                <td class="fw-semibold">{{ formatPrice(o.soTienThanhToan) }}</td>
                <td><span class="badge bg-light text-dark border">{{ o.pttt || '—' }}</span></td>
                <td><span :class="statusClass(o.trangThai)">{{ statusLabel(o.trangThai) }}</span></td>
                <td class="text-muted small">{{ formatDate(o.ngayKhoiTao) }}</td>
                <td class="text-center">
                  <button class="btn btn-sm btn-outline-info me-1" @click="viewOrder(o)" title="Xem">
                    <i class="bi bi-eye"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-primary me-1" @click="openEdit(o)" title="Sửa">
                    <i class="bi bi-pencil"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="remove(o.maDonHang)" title="Xóa" v-if="isAdmin">
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
            <h5 class="modal-title fw-bold">{{ isEdit ? 'Cập nhật' : 'Tạo' }} Đơn hàng</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <form @submit.prevent="save">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Tên khách hàng</label>
                  <input v-model="form.tenKhachHang" class="form-control" required />
                </div>
                <div class="col-md-3">
                  <label class="form-label fw-semibold">Số điện thoại</label>
                  <input v-model="form.soDienThoai" class="form-control" />
                </div>
                <div class="col-md-3">
                  <label class="form-label fw-semibold">Email</label>
                  <input v-model="form.email" class="form-control" />
                </div>
              </div>

              <h6 class="fw-bold mt-3 mb-2"><i class="bi bi-geo-alt me-1"></i>Địa chỉ giao hàng</h6>
              <div class="row g-3">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Tỉnh / Thành phố</label>
                  <input v-model="form.tinhThanhPho" class="form-control" placeholder="VD: Hà Nội" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Quận / Huyện</label>
                  <input v-model="form.quanHuyen" class="form-control" placeholder="VD: Cầu Giấy" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Phường / Xã</label>
                  <input v-model="form.phuongXa" class="form-control" placeholder="VD: Dịch Vọng" />
                </div>
              </div>
              <div class="mb-3 mt-3">
                <label class="form-label fw-semibold">Địa chỉ cụ thể</label>
                <input v-model="form.diaChiCuThe" class="form-control" placeholder="Số nhà, tên đường..." />
              </div>
              <div class="mb-3" v-if="form.ghiChu || !isEdit">
                <label class="form-label fw-semibold">Ghi chú</label>
                <textarea v-model="form.ghiChu" class="form-control" rows="2"></textarea>
              </div>

              <h6 class="fw-bold mt-3 mb-2"><i class="bi bi-cash-stack me-1"></i>Thanh toán</h6>
              <div class="row g-3">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Số tiền ban đầu</label>
                  <input v-model.number="form.soTienBanDau" type="number" class="form-control" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Giảm giá</label>
                  <input v-model.number="form.soTienDuocGiam" type="number" class="form-control" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Thanh toán</label>
                  <input v-model.number="form.soTienThanhToan" type="number" class="form-control" />
                </div>
              </div>
              <div class="row g-3 mt-0">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Phương thức TT</label>
                  <select v-model="form.pttt" class="form-select">
                    <option value="">-- Chọn --</option>
                    <option value="COD">COD</option>
                    <option value="BANKING">Banking</option>
                    <option value="MOMO">Momo</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Mã giảm giá</label>
                  <input v-model="form.maGiamGia" class="form-control" />
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Trạng thái</label>
                  <select v-model="form.trangThai" class="form-select">
                    <option value="CHO_XAC_NHAN">Chờ xác nhận</option>
                    <option value="DA_XAC_NHAN">Đã xác nhận</option>
                    <option value="DANG_GIAO">Đang giao</option>
                    <option value="HOAN_THANH">Hoàn thành</option>
                    <option value="DA_HUY">Đã hủy</option>
                  </select>
                </div>
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
    <!-- View Detail Modal -->
    <div class="modal fade show d-block" v-if="showViewModal" tabindex="-1" style="background:rgba(0,0,0,0.5)" @click.self="showViewModal=false">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
          <div class="modal-header border-0">
            <h5 class="modal-title fw-bold"><i class="bi bi-receipt me-2"></i>Đơn hàng #{{ viewItem?.maDonHang }}</h5>
            <button type="button" class="btn-close" @click="showViewModal=false"></button>
          </div>
          <div class="modal-body" v-if="viewItem">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <span :class="statusClass(viewItem.trangThai)">{{ statusLabel(viewItem.trangThai) }}</span>
              <span class="text-muted small">{{ formatDate(viewItem.ngayKhoiTao) }}</span>
            </div>

            <h6 class="fw-bold mb-2"><i class="bi bi-person me-1"></i>Khách hàng</h6>
            <table class="table table-sm table-borderless mb-3">
              <tr><td class="text-muted" style="width:130px">Tên</td><td class="fw-semibold">{{ viewItem.tenKhachHang || '—' }}</td></tr>
              <tr><td class="text-muted">SĐT</td><td>{{ viewItem.soDienThoai || '—' }}</td></tr>
              <tr v-if="viewItem.email"><td class="text-muted">Email</td><td>{{ viewItem.email }}</td></tr>
            </table>

            <h6 class="fw-bold mb-2"><i class="bi bi-geo-alt me-1"></i>Địa chỉ giao hàng</h6>
            <table class="table table-sm table-borderless mb-3">
              <tr v-if="viewItem.diaChiCuThe"><td class="text-muted" style="width:130px">Địa chỉ</td><td>{{ viewItem.diaChiCuThe }}</td></tr>
              <tr v-if="viewItem.phuongXa"><td class="text-muted">Phường/Xã</td><td>{{ viewItem.phuongXa }}</td></tr>
              <tr v-if="viewItem.quanHuyen"><td class="text-muted">Quận/Huyện</td><td>{{ viewItem.quanHuyen }}</td></tr>
              <tr v-if="viewItem.tinhThanhPho"><td class="text-muted">Tỉnh/TP</td><td>{{ viewItem.tinhThanhPho }}</td></tr>
              <tr v-if="viewItem.diaChi && !viewItem.tinhThanhPho"><td class="text-muted">Địa chỉ đầy đủ</td><td>{{ viewItem.diaChi }}</td></tr>
            </table>

            <h6 class="fw-bold mb-2"><i class="bi bi-cash-stack me-1"></i>Thanh toán</h6>
            <table class="table table-sm table-borderless mb-3">
              <tr><td class="text-muted" style="width:130px">Tạm tính</td><td>{{ formatPrice(viewItem.soTienBanDau) }}</td></tr>
              <tr><td class="text-muted">Giảm giá</td><td class="text-success">{{ viewItem.soTienDuocGiam ? '-' + formatPrice(viewItem.soTienDuocGiam) : '0 ₫' }}</td></tr>
              <tr><td class="text-muted fw-bold">Tổng thanh toán</td><td class="fw-bold text-danger">{{ formatPrice(viewItem.soTienThanhToan) }}</td></tr>
              <tr><td class="text-muted">PTTT</td><td><span class="badge bg-light text-dark border">{{ viewItem.pttt || '—' }}</span></td></tr>
              <tr v-if="viewItem.maGiamGia"><td class="text-muted">Mã giảm giá</td><td>{{ viewItem.maGiamGia }}</td></tr>
            </table>

            <div v-if="viewItem.ghiChu" class="alert alert-light border mb-0">
              <strong class="small">Ghi chú:</strong>
              <p class="mb-0 small">{{ viewItem.ghiChu }}</p>
            </div>
          </div>
          <div class="modal-footer border-0">
            <button class="btn btn-secondary" @click="showViewModal=false">Đóng</button>
            <button class="btn btn-primary" @click="showViewModal=false; openEdit(viewItem)">Chỉnh sửa</button>
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
const showModal = ref(false)
const showViewModal = ref(false)
const viewItem = ref(null)
const isEdit = ref(false)
const editId = ref(null)
const filterStatus = ref('')
const form = ref({
  tenKhachHang: '', soDienThoai: '', email: '', diaChi: '',
  tinhThanhPho: '', quanHuyen: '', phuongXa: '', diaChiCuThe: '', ghiChu: '',
  soTienBanDau: 0, soTienDuocGiam: 0, soTienThanhToan: 0,
  trangThai: 'CHO_XAC_NHAN', pttt: '', maGiamGia: ''
})

const user = computed(() => JSON.parse(localStorage.getItem('admin_user') || 'null'))
const isAdmin = computed(() => user.value?.loaiTaiKhoan === 'ADMIN')
const headers = computed(() => {
  const h = { 'Content-Type': 'application/json' }
  if (user.value) h['X-User-Id'] = String(user.value.maTaiKhoan)
  return h
})

const statusFilters = [
  { label: 'Tất cả', value: '' },
  { label: 'Chờ xác nhận', value: 'CHO_XAC_NHAN' },
  { label: 'Đã xác nhận', value: 'DA_XAC_NHAN' },
  { label: 'Đang giao', value: 'DANG_GIAO' },
  { label: 'Hoàn thành', value: 'HOAN_THANH' },
  { label: 'Đã hủy', value: 'DA_HUY' },
]

const filteredItems = computed(() => {
  if (!filterStatus.value) return items.value
  return items.value.filter(o => o.trangThai === filterStatus.value)
})

function countByStatus(status) {
  if (!status) return items.value.length
  return items.value.filter(o => o.trangThai === status).length
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}
function formatDate(d) { return d ? new Date(d).toLocaleDateString('vi-VN') : '' }

function statusLabel(s) {
  const map = { 'CHO_XAC_NHAN': 'Chờ xác nhận', 'DA_XAC_NHAN': 'Đã xác nhận', 'DANG_GIAO': 'Đang giao', 'HOAN_THANH': 'Hoàn thành', 'DA_HUY': 'Đã hủy' }
  return map[s] || s || 'N/A'
}
function statusClass(s) {
  const map = { 'CHO_XAC_NHAN': 'badge bg-warning text-dark', 'DA_XAC_NHAN': 'badge bg-info', 'DANG_GIAO': 'badge bg-primary', 'HOAN_THANH': 'badge bg-success', 'DA_HUY': 'badge bg-danger' }
  return map[s] || 'badge bg-secondary'
}

async function fetchData() {
  try {
    const res = await fetch(`${API}/orders`, { headers: headers.value })
    if (res.ok) items.value = await res.json()
  } catch (e) { console.error(e) }
}

function openAdd() {
  isEdit.value = false; editId.value = null
  form.value = {
    tenKhachHang: '', soDienThoai: '', email: '', diaChi: '',
    tinhThanhPho: '', quanHuyen: '', phuongXa: '', diaChiCuThe: '', ghiChu: '',
    soTienBanDau: 0, soTienDuocGiam: 0, soTienThanhToan: 0,
    trangThai: 'CHO_XAC_NHAN', pttt: '', maGiamGia: ''
  }
  showModal.value = true
}

function viewOrder(o) {
  viewItem.value = o
  showViewModal.value = true
}

function openEdit(o) {
  isEdit.value = true; editId.value = o.maDonHang
  form.value = {
    tenKhachHang: o.tenKhachHang || '',
    soDienThoai: o.soDienThoai || '',
    email: o.email || '',
    diaChi: o.diaChi || '',
    tinhThanhPho: o.tinhThanhPho || '',
    quanHuyen: o.quanHuyen || '',
    phuongXa: o.phuongXa || '',
    diaChiCuThe: o.diaChiCuThe || '',
    ghiChu: o.ghiChu || '',
    soTienBanDau: o.soTienBanDau || 0,
    soTienDuocGiam: o.soTienDuocGiam || 0,
    soTienThanhToan: o.soTienThanhToan || 0,
    trangThai: o.trangThai || 'CHO_XAC_NHAN',
    pttt: o.pttt || '',
    maGiamGia: o.maGiamGia || ''
  }
  showModal.value = true
}

async function save() {
  try {
    const body = { ...form.value, nguoiCapNhat: user.value?.tenNguoiDung || 'admin' }
    // Build full address string from parts
    if (body.diaChiCuThe && body.phuongXa && body.quanHuyen && body.tinhThanhPho) {
      body.diaChi = `${body.diaChiCuThe}, ${body.phuongXa}, ${body.quanHuyen}, ${body.tinhThanhPho}`
    }
    if (!isEdit.value) {
      body.nguoiKhoiTao = user.value?.tenNguoiDung || 'admin'
      body.maNhanVien = user.value?.maTaiKhoan
    }
    const url = isEdit.value ? `${API}/orders/${editId.value}` : `${API}/orders`
    const method = isEdit.value ? 'PUT' : 'POST'
    await fetch(url, { method, headers: headers.value, body: JSON.stringify(body) })
    showModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function remove(id) {
  if (!confirm('Bạn có chắc muốn xóa đơn hàng này?')) return
  try {
    await fetch(`${API}/orders/${id}`, { method: 'DELETE', headers: headers.value })
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

onMounted(fetchData)
</script>
