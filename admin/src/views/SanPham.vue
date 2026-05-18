<template>
  <div>
    <!-- ==================== VIEW: DANH SÁCH SẢN PHẨM ==================== -->
    <template v-if="!selectedProduct">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h4 class="fw-bold mb-1"><i class="bi bi-box-seam me-2"></i>Quản lý Sản phẩm</h4>
          <p class="text-muted mb-0 small">{{ products.length }} sản phẩm</p>
        </div>
        <button class="btn btn-primary" @click="openAddProduct">
          <i class="bi bi-plus-lg me-1"></i> Thêm sản phẩm
        </button>
      </div>

      <!-- Search & Filter -->
      <div class="card border-0 shadow-sm mb-3">
        <div class="card-body py-2 px-3">
          <div class="d-flex align-items-center gap-3">
            <i class="bi bi-search text-muted"></i>
            <input v-model="searchQuery" class="form-control form-control-sm border-0 shadow-none"
                   placeholder="Tìm kiếm sản phẩm..." style="max-width:350px" />
            <div class="ms-auto d-flex align-items-center gap-2">
              <span class="text-muted small">Trạng thái:</span>
              <select v-model="statusFilter" class="form-select form-select-sm" style="width:140px">
                <option value="">Tất cả</option>
                <option :value="1">Hoạt động</option>
                <option :value="0">Ngừng</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <!-- Product Table -->
      <div class="card border-0 shadow-sm">
        <div class="card-body p-0">
          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead class="table-light">
                <tr>
                  <th class="ps-3" style="width:50px">#</th>
                  <th>Sản phẩm</th>
                  <th>Thương hiệu</th>
                  <th>Chất liệu</th>
                  <th>Xuất xứ</th>
                  <th class="text-center">Biến thể</th>
                  <th>Trạng thái</th>
                  <th style="width:160px" class="text-center">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="filteredProducts.length === 0">
                  <td colspan="8" class="text-center text-muted py-5">
                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                    Không tìm thấy sản phẩm nào
                  </td>
                </tr>
                <tr v-for="(item, idx) in filteredProducts" :key="item.maSanPham">
                  <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <img v-if="item.hinhAnh" :src="item.hinhAnh" class="rounded" style="width:40px;height:40px;object-fit:cover" />
                      <div class="bg-light rounded d-flex align-items-center justify-content-center" v-else style="width:40px;height:40px">
                        <i class="bi bi-image text-muted"></i>
                      </div>
                      <div>
                        <div class="fw-semibold">{{ item.tenSanPham }}</div>
                        <div class="text-muted small text-truncate" style="max-width:180px" v-if="item.moTa">{{ item.moTa }}</div>
                      </div>
                    </div>
                  </td>
                  <td>{{ item.thuongHieu?.tenThuongHieu || '—' }}</td>
                  <td>{{ item.chatLieu?.tenChatLieu || '—' }}</td>
                  <td>{{ item.xuatXu?.tenXuatXu || '—' }}</td>
                  <td class="text-center">
                    <span class="badge bg-primary-subtle text-primary fs-6">{{ getVariantCount(item.maSanPham) }}</span>
                  </td>
                  <td>
                    <span :class="item.trangThai === 1 ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                      {{ item.trangThai === 1 ? 'Hoạt động' : 'Ngừng' }}
                    </span>
                  </td>
                  <td class="text-center">
                    <button class="btn btn-sm btn-outline-info me-1" @click="viewDetail(item)" title="Xem chi tiết">
                      <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-primary me-1" @click="openEditProduct(item)" title="Sửa">
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" @click="removeProduct(item.maSanPham)" title="Xóa">
                      <i class="bi bi-trash"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </template>

    <!-- ==================== VIEW: CHI TIẾT SẢN PHẨM ==================== -->
    <template v-else>
      <!-- Back + Header -->
      <div class="d-flex align-items-center gap-3 mb-4">
        <button class="btn btn-outline-secondary btn-sm" @click="goBack">
          <i class="bi bi-arrow-left me-1"></i> Quay lại
        </button>
        <div class="flex-grow-1">
          <h4 class="fw-bold mb-0">
            <i class="bi bi-box-seam me-2"></i>{{ selectedProduct.tenSanPham }}
          </h4>
        </div>
        <button class="btn btn-primary" @click="openAddVariant">
          <i class="bi bi-plus-lg me-1"></i> Thêm biến thể
        </button>
      </div>

      <!-- Product Info Card -->
      <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
          <div class="d-flex gap-4">
            <img v-if="selectedProduct.hinhAnh" :src="selectedProduct.hinhAnh" class="rounded"
                 style="width:100px;height:100px;object-fit:cover" />
            <div class="bg-light rounded d-flex align-items-center justify-content-center" v-else
                 style="width:100px;height:100px">
              <i class="bi bi-image text-muted fs-3"></i>
            </div>
            <div class="flex-grow-1">
              <h5 class="fw-bold mb-2">{{ selectedProduct.tenSanPham }}</h5>
              <p class="text-muted mb-2" v-if="selectedProduct.moTa">{{ selectedProduct.moTa }}</p>
              <div class="d-flex gap-2 flex-wrap">
                <span class="badge bg-primary-subtle text-primary" v-if="selectedProduct.thuongHieu?.tenThuongHieu">
                  <i class="bi bi-bookmark-star me-1"></i>{{ selectedProduct.thuongHieu.tenThuongHieu }}
                </span>
                <span class="badge bg-info-subtle text-info" v-if="selectedProduct.chatLieu?.tenChatLieu">
                  <i class="bi bi-layers me-1"></i>{{ selectedProduct.chatLieu.tenChatLieu }}
                </span>
                <span class="badge bg-secondary-subtle text-secondary" v-if="selectedProduct.xuatXu?.tenXuatXu">
                  <i class="bi bi-globe2 me-1"></i>{{ selectedProduct.xuatXu.tenXuatXu }}
                </span>
                <span :class="selectedProduct.trangThai === 1 ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                  {{ selectedProduct.trangThai === 1 ? 'Hoạt động' : 'Ngừng' }}
                </span>
              </div>
            </div>
            <div class="text-end">
              <div class="text-muted small">Tổng biến thể</div>
              <div class="fs-3 fw-bold text-primary">{{ currentVariants.length }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Variants Table -->
      <div class="card border-0 shadow-sm">
        <div class="card-header bg-white d-flex align-items-center py-3">
          <i class="bi bi-collection me-2 text-muted"></i>
          <span class="fw-semibold">Danh sách biến thể</span>
          <span class="badge bg-primary ms-2">{{ currentVariants.length }}</span>
        </div>
        <div class="card-body p-0">
          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead class="table-light">
                <tr>
                  <th class="ps-3" style="width:50px">#</th>
                  <th>Hình ảnh</th>
                  <th>Màu sắc</th>
                  <th>Size</th>
                  <th>Số lượng</th>
                  <th>Giá tiền</th>
                  <th>Trạng thái</th>
                  <th style="width:120px" class="text-center">Hành động</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="currentVariants.length === 0">
                  <td colspan="8" class="text-center text-muted py-5">
                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                    Chưa có biến thể nào
                    <div class="mt-2">
                      <button class="btn btn-sm btn-primary" @click="openAddVariant">
                        <i class="bi bi-plus-lg me-1"></i> Thêm biến thể đầu tiên
                      </button>
                    </div>
                  </td>
                </tr>
                <tr v-for="(v, idx) in currentVariants" :key="v.maSanPhamChiTiet">
                  <td class="ps-3 text-muted">{{ idx + 1 }}</td>
                  <td>
                    <img v-if="v.hinhAnh" :src="v.hinhAnh" class="rounded" style="width:40px;height:40px;object-fit:cover" />
                    <div v-else class="bg-light rounded d-flex align-items-center justify-content-center" style="width:40px;height:40px">
                      <i class="bi bi-image text-muted"></i>
                    </div>
                  </td>
                  <td>
                    <span class="d-flex align-items-center gap-2">
                      <span class="rounded-circle d-inline-block border" style="width:16px;height:16px"
                            :style="{ background: getColorHex(v.mauSac?.tenMau) }"></span>
                      {{ v.mauSac?.tenMau || '—' }}
                    </span>
                  </td>
                  <td><span class="badge bg-secondary-subtle text-secondary">{{ v.size?.tenSize || '—' }}</span></td>
                  <td>
                    <span :class="v.soLuong > 0 ? '' : 'text-danger fw-semibold'">{{ v.soLuong }}</span>
                  </td>
                  <td class="fw-semibold text-danger text-nowrap">{{ formatPrice(v.giaTien) }}</td>
                  <td>
                    <span :class="v.trangThai === 1 ? 'badge bg-success-subtle text-success' : 'badge bg-danger-subtle text-danger'">
                      {{ v.trangThai === 1 ? 'Hoạt động' : 'Ngừng' }}
                    </span>
                  </td>
                  <td class="text-center">
                    <button class="btn btn-sm btn-outline-primary me-1" @click="openEditVariant(v)" title="Sửa">
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" @click="removeVariant(v.maSanPhamChiTiet)" title="Xóa">
                      <i class="bi bi-trash"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </template>

    <!-- ==================== MODAL: THÊM / SỬA SẢN PHẨM ==================== -->
    <div class="modal fade show d-block" v-if="showProductModal" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title fw-bold">
              <i class="bi bi-box-seam me-2"></i>{{ isEditProduct ? 'Sửa' : 'Thêm' }} Sản phẩm
            </h5>
            <button type="button" class="btn-close" @click="showProductModal = false"></button>
          </div>
          <form @submit.prevent="saveProduct">
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label fw-semibold">Tên sản phẩm <span class="text-danger">*</span></label>
                <input v-model="productForm.tenSanPham" class="form-control" required placeholder="Nhập tên sản phẩm" />
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Hình ảnh</label>
                <div class="d-flex gap-2 align-items-start">
                  <div class="flex-grow-1">
                    <div class="input-group">
                      <label class="btn btn-outline-secondary" :class="{ disabled: productUploading }">
                        <i class="bi bi-upload me-1"></i>{{ productUploading ? 'Đang tải...' : 'Chọn ảnh' }}
                        <input type="file" accept="image/*" class="d-none" @change="uploadProductImage" :disabled="productUploading" />
                      </label>
                      <input v-model="productForm.hinhAnh" class="form-control" placeholder="Hoặc dán URL ảnh..." />
                    </div>
                  </div>
                  <div v-if="productForm.hinhAnh" class="position-relative flex-shrink-0">
                    <img :src="productForm.hinhAnh" class="rounded border" style="width:64px;height:64px;object-fit:cover"
                         @error="$event.target.style.display='none'" />
                    <button type="button" class="btn btn-sm btn-danger position-absolute top-0 end-0 rounded-circle p-0 d-flex align-items-center justify-content-center"
                            style="width:20px;height:20px;transform:translate(30%,-30%)" @click="productForm.hinhAnh = ''">
                      <i class="bi bi-x" style="font-size:0.7rem"></i>
                    </button>
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Mô tả</label>
                <textarea v-model="productForm.moTa" class="form-control" rows="3" placeholder="Nhập mô tả sản phẩm"></textarea>
              </div>
              <div class="row g-3">
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Thương hiệu <span class="text-danger">*</span></label>
                  <select v-model="productForm.thuongHieuId" class="form-select" required>
                    <option value="">-- Chọn --</option>
                    <option v-for="b in brands" :key="b.maThuongHieu" :value="b.maThuongHieu">{{ b.tenThuongHieu }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Chất liệu <span class="text-danger">*</span></label>
                  <select v-model="productForm.chatLieuId" class="form-select" required>
                    <option value="">-- Chọn --</option>
                    <option v-for="m in materials" :key="m.maChatLieu" :value="m.maChatLieu">{{ m.tenChatLieu }}</option>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label fw-semibold">Xuất xứ <span class="text-danger">*</span></label>
                  <select v-model="productForm.xuatXuId" class="form-select" required>
                    <option value="">-- Chọn --</option>
                    <option v-for="o in origins" :key="o.maXuatXu" :value="o.maXuatXu">{{ o.tenXuatXu }}</option>
                  </select>
                </div>
              </div>
              <div class="mt-3">
                <label class="form-label fw-semibold">Trạng thái</label>
                <select v-model="productForm.trangThai" class="form-select">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showProductModal = false">Hủy</button>
              <button type="submit" class="btn btn-primary">
                <i class="bi bi-check-lg me-1"></i>{{ isEditProduct ? 'Cập nhật' : 'Lưu' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- ==================== MODAL: THÊM / SỬA BIẾN THỂ ==================== -->
    <div class="modal fade show d-block" v-if="showVariantModal" tabindex="-1" style="background:rgba(0,0,0,0.5)">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title fw-bold">
              <i class="bi bi-collection me-2"></i>{{ isEditVariant ? 'Sửa' : 'Thêm' }} biến thể
              <span class="text-muted fw-normal ms-2" style="font-size:0.85rem">— {{ selectedProduct?.tenSanPham }}</span>
            </h5>
            <button type="button" class="btn-close" @click="showVariantModal = false"></button>
          </div>
          <form @submit.prevent="saveVariant">
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Màu sắc <span class="text-danger">*</span></label>
                  <select v-model="variantForm.mauSacId" class="form-select" required>
                    <option value="">-- Chọn màu --</option>
                    <option v-for="c in colors" :key="c.maMauSac" :value="c.maMauSac">{{ c.tenMau }}</option>
                  </select>
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Size <span class="text-danger">*</span></label>
                  <select v-model="variantForm.sizeId" class="form-select" required>
                    <option value="">-- Chọn size --</option>
                    <option v-for="s in sizes" :key="s.maSize" :value="s.maSize">{{ s.tenSize }}</option>
                  </select>
                </div>
              </div>
              <div class="row g-3 mt-0">
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Số lượng <span class="text-danger">*</span></label>
                  <input v-model.number="variantForm.soLuong" type="number" class="form-control" min="0" required placeholder="0" />
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-semibold">Giá tiền (VNĐ) <span class="text-danger">*</span></label>
                  <input v-model.number="variantForm.giaTien" type="number" class="form-control" min="0" step="1000" required placeholder="0" />
                </div>
              </div>
              <div class="mb-3 mt-3">
                <label class="form-label fw-semibold">Hình ảnh</label>
                <div class="d-flex gap-2 align-items-start">
                  <div class="flex-grow-1">
                    <div class="input-group">
                      <label class="btn btn-outline-secondary" :class="{ disabled: variantUploading }">
                        <i class="bi bi-upload me-1"></i>{{ variantUploading ? 'Đang tải...' : 'Chọn ảnh' }}
                        <input type="file" accept="image/*" class="d-none" @change="uploadVariantImage" :disabled="variantUploading" />
                      </label>
                      <input v-model="variantForm.hinhAnh" class="form-control" placeholder="Hoặc dán URL ảnh..." />
                    </div>
                  </div>
                  <div v-if="variantForm.hinhAnh" class="position-relative flex-shrink-0">
                    <img :src="variantForm.hinhAnh" class="rounded border" style="width:60px;height:60px;object-fit:cover"
                         @error="$event.target.style.display='none'" />
                    <button type="button" class="btn btn-sm btn-danger position-absolute top-0 end-0 rounded-circle p-0 d-flex align-items-center justify-content-center"
                            style="width:20px;height:20px;transform:translate(30%,-30%)" @click="variantForm.hinhAnh = ''">
                      <i class="bi bi-x" style="font-size:0.7rem"></i>
                    </button>
                  </div>
                </div>
              </div>
              <div>
                <label class="form-label fw-semibold">Trạng thái</label>
                <select v-model="variantForm.trangThai" class="form-select">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showVariantModal = false">Hủy</button>
              <button type="submit" class="btn btn-primary">
                <i class="bi bi-check-lg me-1"></i>{{ isEditVariant ? 'Cập nhật' : 'Lưu' }}
              </button>
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

// ── Data ──
const products = ref([])
const variants = ref([])
const brands = ref([])
const materials = ref([])
const origins = ref([])
const colors = ref([])
const sizes = ref([])

// ── UI State ──
const searchQuery = ref('')
const statusFilter = ref('')
const selectedProduct = ref(null)

// ── Product Modal ──
const showProductModal = ref(false)
const isEditProduct = ref(false)
const editProductId = ref(null)
const productForm = ref({ tenSanPham: '', moTa: '', hinhAnh: '', thuongHieuId: '', chatLieuId: '', xuatXuId: '', trangThai: 1 })

// ── Variant Modal ──
const showVariantModal = ref(false)
const isEditVariant = ref(false)
const editVariantId = ref(null)
const variantForm = ref({ mauSacId: '', sizeId: '', soLuong: 0, giaTien: 0, hinhAnh: '', trangThai: 1 })

// ── Upload State ──
const productUploading = ref(false)
const variantUploading = ref(false)

// ── Computed ──
const filteredProducts = computed(() => {
  let list = products.value
  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase()
    list = list.filter(p =>
      p.tenSanPham?.toLowerCase().includes(q) ||
      p.thuongHieu?.tenThuongHieu?.toLowerCase().includes(q) ||
      p.chatLieu?.tenChatLieu?.toLowerCase().includes(q)
    )
  }
  if (statusFilter.value !== '') {
    list = list.filter(p => p.trangThai === Number(statusFilter.value))
  }
  return list
})

const currentVariants = computed(() => {
  if (!selectedProduct.value) return []
  return variants.value.filter(v => v.sanPham?.maSanPham === selectedProduct.value.maSanPham)
})

// ── Helpers ──
function getVariantCount(productId) {
  return variants.value.filter(v => v.sanPham?.maSanPham === productId).length
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const colorMap = {
  'đen': '#222', 'trắng': '#f5f5f5', 'đỏ': '#e53935', 'xanh': '#1e88e5', 'xanh lá': '#43a047',
  'vàng': '#fdd835', 'hồng': '#ec407a', 'tím': '#8e24aa', 'cam': '#fb8c00', 'xám': '#9e9e9e',
  'nâu': '#795548', 'be': '#d7ccc8', 'xanh dương': '#1565c0', 'xanh navy': '#1a237e'
}

function getColorHex(name) {
  if (!name) return '#ccc'
  return colorMap[name.toLowerCase().trim()] || '#ccc'
}

// ── Navigation ──
function viewDetail(product) {
  selectedProduct.value = product
}

function goBack() {
  selectedProduct.value = null
}

// ── Fetch ──
async function fetchData() {
  try {
    const [p, d, b, m, o, c, s] = await Promise.all([
      fetch(`${API}/products`).then(r => r.json()),
      fetch(`${API}/product-details`).then(r => r.json()),
      fetch(`${API}/brands`).then(r => r.json()),
      fetch(`${API}/materials`).then(r => r.json()),
      fetch(`${API}/origins`).then(r => r.json()),
      fetch(`${API}/colors`).then(r => r.json()),
      fetch(`${API}/sizes`).then(r => r.json()),
    ])
    products.value = p
    variants.value = d
    brands.value = b
    materials.value = m
    origins.value = o
    colors.value = c
    sizes.value = s
    // Sync selectedProduct nếu đang ở trang chi tiết
    if (selectedProduct.value) {
      selectedProduct.value = p.find(x => x.maSanPham === selectedProduct.value.maSanPham) || null
    }
  } catch (e) { console.error(e) }
}

// ── Product CRUD ──
function openAddProduct() {
  isEditProduct.value = false
  editProductId.value = null
  productForm.value = { tenSanPham: '', moTa: '', hinhAnh: '', thuongHieuId: '', chatLieuId: '', xuatXuId: '', trangThai: 1 }
  showProductModal.value = true
}

function openEditProduct(item) {
  isEditProduct.value = true
  editProductId.value = item.maSanPham
  productForm.value = {
    tenSanPham: item.tenSanPham,
    moTa: item.moTa || '',
    hinhAnh: item.hinhAnh || '',
    thuongHieuId: item.thuongHieu?.maThuongHieu || '',
    chatLieuId: item.chatLieu?.maChatLieu || '',
    xuatXuId: item.xuatXu?.maXuatXu || '',
    trangThai: item.trangThai
  }
  showProductModal.value = true
}

async function saveProduct() {
  try {
    const user = JSON.parse(localStorage.getItem('admin_user') || '{}')
    const body = {
      tenSanPham: productForm.value.tenSanPham,
      moTa: productForm.value.moTa,
      hinhAnh: productForm.value.hinhAnh,
      trangThai: productForm.value.trangThai,
      nguoiKhoiTao: user.tenNguoiDung || 'admin',
      nguoiCapNhat: user.tenNguoiDung || 'admin',
      thuongHieu: productForm.value.thuongHieuId ? { maThuongHieu: productForm.value.thuongHieuId } : null,
      chatLieu: productForm.value.chatLieuId ? { maChatLieu: productForm.value.chatLieuId } : null,
      xuatXu: productForm.value.xuatXuId ? { maXuatXu: productForm.value.xuatXuId } : null
    }
    if (isEditProduct.value) {
      await fetch(`${API}/products/${editProductId.value}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    } else {
      await fetch(`${API}/products`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
    }
    showProductModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function removeProduct(id) {
  const count = getVariantCount(id)
  const msg = count > 0
    ? `Sản phẩm này có ${count} biến thể. Bạn có chắc muốn xóa?`
    : 'Bạn có chắc muốn xóa sản phẩm này?'
  if (!confirm(msg)) return
  try {
    await fetch(`${API}/products/${id}`, { method: 'DELETE' })
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

// ── Variant CRUD ──
function openAddVariant() {
  isEditVariant.value = false
  editVariantId.value = null
  variantForm.value = { mauSacId: '', sizeId: '', soLuong: 0, giaTien: 0, hinhAnh: '', trangThai: 1 }
  showVariantModal.value = true
}

function openEditVariant(item) {
  isEditVariant.value = true
  editVariantId.value = item.maSanPhamChiTiet
  variantForm.value = {
    mauSacId: item.mauSac?.maMauSac || '',
    sizeId: item.size?.maSize || '',
    soLuong: item.soLuong || 0,
    giaTien: item.giaTien || 0,
    hinhAnh: item.hinhAnh || '',
    trangThai: item.trangThai
  }
  showVariantModal.value = true
}

async function saveVariant() {
  try {
    const body = {
      soLuong: variantForm.value.soLuong,
      giaTien: variantForm.value.giaTien,
      hinhAnh: variantForm.value.hinhAnh,
      trangThai: variantForm.value.trangThai,
      sanPham: { maSanPham: selectedProduct.value.maSanPham },
      mauSac: variantForm.value.mauSacId ? { maMauSac: variantForm.value.mauSacId } : null,
      size: variantForm.value.sizeId ? { maSize: variantForm.value.sizeId } : null
    }
    if (isEditVariant.value) {
      await fetch(`${API}/product-details/${editVariantId.value}`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
      })
    } else {
      await fetch(`${API}/product-details`, {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
      })
    }
    showVariantModal.value = false
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

async function removeVariant(id) {
  if (!confirm('Bạn có chắc muốn xóa biến thể này?')) return
  try {
    await fetch(`${API}/product-details/${id}`, { method: 'DELETE' })
    fetchData()
  } catch (e) { alert('Lỗi: ' + e.message) }
}

// ── Image Upload ──
async function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  const res = await fetch(`${API}/upload`, { method: 'POST', body: formData })
  if (!res.ok) throw new Error('Upload thất bại')
  const data = await res.json()
  return data.url
}

async function uploadProductImage(e) {
  const file = e.target.files?.[0]
  if (!file) return
  productUploading.value = true
  try {
    productForm.value.hinhAnh = await uploadFile(file)
  } catch (err) { alert('Lỗi upload: ' + err.message) }
  productUploading.value = false
  e.target.value = ''
}

async function uploadVariantImage(e) {
  const file = e.target.files?.[0]
  if (!file) return
  variantUploading.value = true
  try {
    variantForm.value.hinhAnh = await uploadFile(file)
  } catch (err) { alert('Lỗi upload: ' + err.message) }
  variantUploading.value = false
  e.target.value = ''
}

onMounted(fetchData)
</script>

<style scoped>
.cursor-pointer { cursor: pointer; }
.cursor-pointer:hover td { background: #f8f9ff; }
</style>
