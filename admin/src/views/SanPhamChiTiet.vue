<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="fw-bold mb-1"><i class="bi bi-box-seam-fill me-2"></i>Quản lý Sản phẩm chi tiết</h4>
        <p class="text-muted mb-0 small">{{ filteredItems.length }} biến thể</p>
      </div>
      <button class="btn btn-primary" @click="openAdd">
        <i class="bi bi-plus-lg me-1"></i> Thêm mới
      </button>
    </div>

    <!-- Filter -->
    <div class="card border-0 shadow-sm mb-3">
      <div class="card-body py-2 px-3">
        <div class="d-flex align-items-center gap-3">
          <label class="form-label fw-semibold mb-0 text-nowrap small">
            <i class="bi bi-funnel me-1"></i>Lọc theo sản phẩm:
          </label>
          <select v-model="filterProduct" @change="applyFilter" class="form-select form-select-sm" style="max-width:300px">
            <option value="">-- Tất cả --</option>
            <option v-for="p in products" :key="p.maSanPham" :value="p.maSanPham">
              {{ p.tenSanPham }}
            </option>
          </select>
        </div>
      </div>
    </div>

    <div class="card border-0 shadow-sm">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-hover align-middle mb-0">
            <thead class="table-light">
              <tr>
                <th class="ps-3" style="width:50px">#</th>
                <th>Sản phẩm</th>
                <th>Màu sắc</th>
                <th>Size</th>
                <th>Số lượng</th>
                <th>Giá tiền</th>
                <th>Hình ảnh</th>
                <th>Trạng thái</th>
                <th style="width:120px" class="text-center">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="filteredItems.length === 0">
                <td colspan="9" class="text-center text-muted py-4">Không có dữ liệu</td>
              </tr>
              <tr v-for="(item, idx) in filteredItems" :key="item.maSanPhamChiTiet">
                <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                <td>
                  <div class="fw-semibold text-truncate" style="max-width:160px">{{ item.sanPham?.tenSanPham || '—' }}</div>
                </td>
                <td>{{ item.mauSac?.tenMau || '—' }}</td>
                <td><span class="badge bg-secondary-subtle text-secondary">{{ item.size?.tenSize || '—' }}</span></td>
                <td>{{ item.soLuong }}</td>
                <td class="fw-semibold text-danger text-nowrap">{{ formatPrice(item.giaTien) }}</td>
                <td>
                  <img v-if="item.hinhAnh" :src="item.hinhAnh" class="rounded" style="width:40px;height:40px;object-fit:cover" />
                  <div class="bg-light rounded d-flex align-items-center justify-content-center" v-else style="width:40px;height:40px">
                    <i class="bi bi-image text-muted"></i>
                  </div>
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
                  <button class="btn btn-sm btn-outline-danger" @click="remove(item.maSanPhamChiTiet)" title="Xóa">
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
            <h5 class="modal-title fw-bold">{{ isEdit ? 'Sửa' : 'Thêm' }} Sản phẩm chi tiết</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <form @submit.prevent="save">
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label fw-semibold">Sản phẩm</label>
                <select v-model="form.sanPhamId" class="form-select" required>
                  <option value="">-- Chọn sản phẩm --</option>
                  <option v-for="p in products" :key="p.maSanPham" :value="p.maSanPham">
                    {{ p.tenSanPham }}
                  </option>
                </select>
              </div>
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Màu sắc</label>
                  <select v-model="form.mauSacId" class="form-select" required>
                    <option value="">-- Chọn màu --</option>
                    <option v-for="c in colors" :key="c.maMauSac" :value="c.maMauSac">
                      {{ c.tenMau }}
                    </option>
                  </select>
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Size</label>
                  <select v-model="form.sizeId" class="form-select" required>
                    <option value="">-- Chọn size --</option>
                    <option v-for="s in sizes" :key="s.maSize" :value="s.maSize">
                      {{ s.tenSize }}
                    </option>
                  </select>
                </div>
              </div>
              <div class="row g-3 mt-0">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Số lượng</label>
                  <input v-model.number="form.soLuong" type="number" class="form-control" min="0" required placeholder="0" />
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                  <input v-model.number="form.giaTien" type="number" class="form-control" min="0" step="1000" required placeholder="0" />
                </div>
              </div>
              <div class="mb-3 mt-3">
                <label class="form-label fw-semibold">Hình ảnh</label>
                <div class="d-flex gap-2 align-items-start">
                  <div class="flex-grow-1">
                    <div class="input-group">
                      <label class="btn btn-outline-secondary" :class="{ disabled: uploading }">
                        <i class="bi bi-upload me-1"></i>{{ uploading ? 'Đang tải...' : 'Chọn ảnh' }}
                        <input type="file" accept="image/*" class="d-none" @change="uploadImage" :disabled="uploading" />
                      </label>
                      <input v-model="form.hinhAnh" class="form-control" placeholder="Hoặc dán URL ảnh..." />
                    </div>
                  </div>
                  <div v-if="form.hinhAnh" class="position-relative flex-shrink-0">
                    <img :src="form.hinhAnh" class="rounded border" style="width:60px;height:60px;object-fit:cover"
                         @error="$event.target.style.display='none'" />
                    <button type="button" class="btn btn-sm btn-danger position-absolute top-0 end-0 rounded-circle p-0 d-flex align-items-center justify-content-center"
                            style="width:20px;height:20px;transform:translate(30%,-30%)" @click="form.hinhAnh = ''">
                      <i class="bi bi-x" style="font-size:0.7rem"></i>
                    </button>
                  </div>
                </div>
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
import { ref, computed, onMounted } from 'vue'

const API = 'http://localhost:8080/api'
const items = ref([])
const products = ref([])
const colors = ref([])
const sizes = ref([])
const filterProduct = ref('')
const showModal = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = ref({
  sanPhamId: '',
  mauSacId: '',
  sizeId: '',
  soLuong: 0,
  giaTien: 0,
  hinhAnh: '',
  trangThai: 1
})
const uploading = ref(false)

const filteredItems = computed(() => {
  if (!filterProduct.value) return items.value
  return items.value.filter(i => i.sanPham?.maSanPham === filterProduct.value)
})

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

async function fetchData() {
  try {
    const [details, p, c, s] = await Promise.all([
      fetch(`${API}/product-details`).then(r => r.json()),
      fetch(`${API}/products`).then(r => r.json()),
      fetch(`${API}/colors`).then(r => r.json()),
      fetch(`${API}/sizes`).then(r => r.json()),
    ])
    items.value = details
    products.value = p
    colors.value = c
    sizes.value = s
  } catch (e) { console.error(e) }
}

function applyFilter() { /* reactivity via computed */ }

function openAdd() {
  isEdit.value = false
  editId.value = null
  form.value = { sanPhamId: '', mauSacId: '', sizeId: '', soLuong: 0, giaTien: 0, hinhAnh: '', trangThai: 1 }
  showModal.value = true
}

function openEdit(item) {
  isEdit.value = true
  editId.value = item.maSanPhamChiTiet
  form.value = {
    sanPhamId: item.sanPham?.maSanPham || '',
    mauSacId: item.mauSac?.maMauSac || '',
    sizeId: item.size?.maSize || '',
    soLuong: item.soLuong || 0,
    giaTien: item.giaTien || 0,
    hinhAnh: item.hinhAnh || '',
    trangThai: item.trangThai
  }
  showModal.value = true
}

async function save() {
  try {
    const body = {
      soLuong: form.value.soLuong,
      giaTien: form.value.giaTien,
      hinhAnh: form.value.hinhAnh,
      trangThai: form.value.trangThai,
      sanPham: form.value.sanPhamId ? { maSanPham: form.value.sanPhamId } : null,
      mauSac: form.value.mauSacId ? { maMauSac: form.value.mauSacId } : null,
      size: form.value.sizeId ? { maSize: form.value.sizeId } : null
    }
    if (isEdit.value) {
      await fetch(`${API}/product-details/${editId.value}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      })
    } else {
      await fetch(`${API}/product-details`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      })
    }
    showModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function remove(id) {
  if (!confirm('Bạn có chắc muốn xóa?')) return
  try {
    await fetch(`${API}/product-details/${id}`, { method: 'DELETE' })
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function uploadImage(e) {
  const file = e.target.files?.[0]
  if (!file) return
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('file', file)
    const res = await fetch(`${API}/upload`, { method: 'POST', body: fd })
    if (!res.ok) throw new Error('Upload thất bại')
    const data = await res.json()
    form.value.hinhAnh = data.url
  } catch (err) { alert('Lỗi upload: ' + err.message) }
  uploading.value = false
  e.target.value = ''
}

onMounted(fetchData)
</script>
