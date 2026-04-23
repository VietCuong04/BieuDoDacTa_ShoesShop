<template>
  <div class="products-page">
    <!-- Banner -->
    <section class="page-banner">
      <div class="banner-content">
        <span class="banner-badge">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 3H8l-2 4h12l-2-4z"/></svg>
          Bộ sưu tập
        </span>
        <h1>Tất Cả Sản Phẩm</h1>
        <p>Khám phá bộ sưu tập giày đa dạng của chúng tôi</p>
      </div>
    </section>

    <div class="products-container">
      <!-- Sidebar Filter -->
      <aside class="filter-sidebar">
        <div class="filter-header">
          <h3>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg>
            Bộ lọc
          </h3>
          <button class="btn-clear-inline" @click="clearFilters" v-if="hasActiveFilters">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
            Xóa
          </button>
        </div>

        <!-- Thương hiệu -->
        <div class="filter-group">
          <h4>Thương hiệu</h4>
          <label v-for="b in brands" :key="b.maThuongHieu" class="filter-option">
            <input type="checkbox" :value="b.maThuongHieu" v-model="selectedBrands" />
            <span class="custom-check"></span>
            <span>{{ b.tenThuongHieu }}</span>
          </label>
        </div>

        <!-- Chất liệu -->
        <div class="filter-group">
          <h4>Chất liệu</h4>
          <label v-for="m in materials" :key="m.maChatLieu" class="filter-option">
            <input type="checkbox" :value="m.maChatLieu" v-model="selectedMaterials" />
            <span class="custom-check"></span>
            <span>{{ m.tenChatLieu }}</span>
          </label>
        </div>

        <!-- Xuất xứ -->
        <div class="filter-group">
          <h4>Xuất xứ</h4>
          <label v-for="o in origins" :key="o.maXuatXu" class="filter-option">
            <input type="checkbox" :value="o.maXuatXu" v-model="selectedOrigins" />
            <span class="custom-check"></span>
            <span>{{ o.tenXuatXu }}</span>
          </label>
        </div>
      </aside>

      <!-- Product Grid -->
      <div class="products-main">
        <!-- Search & Sort Bar -->
        <div class="toolbar">
          <div class="search-filter">
            <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
            <input type="text" v-model="searchQuery" placeholder="Tìm kiếm sản phẩm..." class="search-input" />
          </div>
          <div class="sort-wrap">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m3 16 4 4 4-4"/><path d="M7 20V4"/><path d="m21 8-4-4-4 4"/><path d="M17 4v16"/></svg>
            <select v-model="sortBy">
              <option value="newest">Mới nhất</option>
              <option value="name-asc">Tên A → Z</option>
              <option value="name-desc">Tên Z → A</option>
              <option value="price-asc">Giá tăng dần</option>
              <option value="price-desc">Giá giảm dần</option>
            </select>
          </div>
          <span class="result-count">
            <strong>{{ filteredProducts.length }}</strong> sản phẩm
          </span>
        </div>

        <!-- Product Grid -->
        <div class="product-grid" v-if="filteredProducts.length > 0">
          <router-link
            v-for="product in filteredProducts"
            :key="product.maSanPham"
            :to="`/products/${product.maSanPham}`"
            class="product-card"
          >
            <div class="product-image">
              <img v-if="product.thumbImage" :src="product.thumbImage" :alt="product.tenSanPham" />
              <div v-else class="placeholder-img">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" opacity="0.25"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>
              </div>
              <span class="badge-brand" v-if="product.thuongHieu">{{ product.thuongHieu.tenThuongHieu }}</span>
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ product.tenSanPham }}</h3>
              <p class="product-meta" v-if="product.chatLieu">{{ product.chatLieu.tenChatLieu }}</p>
              <div class="product-bottom">
                <span class="price" v-if="product.minPrice != null">
                  {{ formatPrice(product.minPrice) }}
                  <span v-if="product.maxPrice && product.maxPrice !== product.minPrice" class="price-range">
                    - {{ formatPrice(product.maxPrice) }}
                  </span>
                </span>
                <span class="price no-price" v-else>Liên hệ</span>
                <span class="variant-count" v-if="product.variantCount">{{ product.variantCount }} loại</span>
              </div>
            </div>
          </router-link>
        </div>

        <!-- Empty State -->
        <div class="empty-state" v-else>
          <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/><path d="M8 11h6"/></svg>
          <h3>Không tìm thấy sản phẩm</h3>
          <p>Thử thay đổi bộ lọc hoặc từ khóa tìm kiếm</p>
          <button class="btn-reset" @click="clearFilters">Xóa bộ lọc</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const API = 'http://localhost:8080/api'

const products = ref([])
const productDetails = ref([])
const brands = ref([])
const materials = ref([])
const origins = ref([])

const searchQuery = ref('')
const sortBy = ref('newest')
const selectedBrands = ref([])
const selectedMaterials = ref([])
const selectedOrigins = ref([])

const hasActiveFilters = computed(() => {
  return searchQuery.value.trim() !== '' ||
    selectedBrands.value.length > 0 ||
    selectedMaterials.value.length > 0 ||
    selectedOrigins.value.length > 0 ||
    sortBy.value !== 'newest'
})

function formatPrice(v) {
  if (v == null) return ''
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const enrichedProducts = computed(() => {
  return products.value
    .filter(p => p.trangThai === 1)
    .map(p => {
      const details = productDetails.value.filter(
        d => d.sanPham?.maSanPham === p.maSanPham && d.trangThai === 1
      )
      const prices = details.map(d => d.giaTien).filter(g => g != null)
      const detailImg = details.map(d => d.hinhAnh).filter(Boolean)
      return {
        ...p,
        minPrice: prices.length ? Math.min(...prices) : null,
        maxPrice: prices.length ? Math.max(...prices) : null,
        thumbImage: p.hinhAnh || (detailImg.length ? detailImg[0] : null),
        variantCount: details.length
      }
    })
})

const filteredProducts = computed(() => {
  let result = enrichedProducts.value

  if (searchQuery.value.trim()) {
    const q = searchQuery.value.toLowerCase()
    result = result.filter(p =>
      p.tenSanPham?.toLowerCase().includes(q) ||
      p.moTa?.toLowerCase().includes(q) ||
      p.thuongHieu?.tenThuongHieu?.toLowerCase().includes(q)
    )
  }

  if (selectedBrands.value.length > 0) {
    result = result.filter(p => selectedBrands.value.includes(p.thuongHieu?.maThuongHieu))
  }
  if (selectedMaterials.value.length > 0) {
    result = result.filter(p => selectedMaterials.value.includes(p.chatLieu?.maChatLieu))
  }
  if (selectedOrigins.value.length > 0) {
    result = result.filter(p => selectedOrigins.value.includes(p.xuatXu?.maXuatXu))
  }

  switch (sortBy.value) {
    case 'name-asc':
      result = [...result].sort((a, b) => (a.tenSanPham || '').localeCompare(b.tenSanPham || ''))
      break
    case 'name-desc':
      result = [...result].sort((a, b) => (b.tenSanPham || '').localeCompare(a.tenSanPham || ''))
      break
    case 'price-asc':
      result = [...result].sort((a, b) => (a.minPrice || 0) - (b.minPrice || 0))
      break
    case 'price-desc':
      result = [...result].sort((a, b) => (b.minPrice || 0) - (a.minPrice || 0))
      break
    default:
      result = [...result].sort((a, b) => (b.maSanPham || 0) - (a.maSanPham || 0))
  }

  return result
})

function clearFilters() {
  searchQuery.value = ''
  selectedBrands.value = []
  selectedMaterials.value = []
  selectedOrigins.value = []
  sortBy.value = 'newest'
}

onMounted(async () => {
  try {
    const [p, pd, b, m, o] = await Promise.all([
      fetch(`${API}/products`).then(r => r.json()),
      fetch(`${API}/product-details`).then(r => r.json()),
      fetch(`${API}/brands`).then(r => r.json()),
      fetch(`${API}/materials`).then(r => r.json()),
      fetch(`${API}/origins`).then(r => r.json()),
    ])
    products.value = p
    productDetails.value = pd
    brands.value = b.filter(x => x.trangThai === 1)
    materials.value = m.filter(x => x.trangThai === 1)
    origins.value = o.filter(x => x.trangThai === 1)
  } catch (e) {
    console.error('Lỗi tải dữ liệu:', e)
  }
})
</script>

<style scoped>
.products-page {
  min-height: 100vh;
  background: #fafafa;
}

/* ═══════════ Banner ═══════════ */
.page-banner {
  background: #111;
  color: #fff;
  padding: 3rem 2rem 2.5rem;
  text-align: center;
  position: relative;
  border-bottom: 3px solid #e94560;
}
.banner-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.3rem 0.8rem;
  background: rgba(255,255,255,0.08);
  border: 1px solid rgba(255,255,255,0.12);
  font-size: 0.7rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  margin-bottom: 0.8rem;
}
.page-banner h1 {
  font-size: 2rem;
  font-weight: 900;
  margin-bottom: 0.3rem;
  letter-spacing: -0.5px;
}
.page-banner p {
  color: rgba(255,255,255,0.5);
  font-size: 0.9rem;
}

/* ═══════════ Layout ═══════════ */
.products-container {
  display: flex;
  gap: 2rem;
  max-width: 1320px;
  margin: 2rem auto 3rem;
  padding: 0 2rem;
  position: relative;
  z-index: 1;
}

/* ═══════════ Sidebar ═══════════ */
.filter-sidebar {
  width: 260px;
  flex-shrink: 0;
  background: #fff;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  align-self: flex-start;
  position: sticky;
  top: 90px;
  border: 1px solid #e8e8e8;
}
.filter-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.2rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid #f0f0f0;
}
.filter-header h3 {
  font-size: 1rem;
  color: #1a1a2e;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 700;
}
.btn-clear-inline {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  padding: 0.3rem 0.7rem;
  background: #fff;
  border: 1px solid #e94560;
  color: #e94560;
  cursor: pointer;
  font-size: 0.75rem;
  font-weight: 600;
  transition: all 0.2s;
}
.btn-clear-inline:hover {
  background: #e94560;
  color: #fff;
}
.filter-group {
  margin-bottom: 1.4rem;
}
.filter-group h4 {
  font-size: 0.8rem;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.8px;
  margin-bottom: 0.6rem;
  font-weight: 700;
}
.filter-option {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  padding: 0.4rem 0;
  cursor: pointer;
  font-size: 0.88rem;
  color: #444;
  transition: color 0.2s;
  position: relative;
}
.filter-option:hover {
  color: #1a1a2e;
}
.filter-option input[type="checkbox"] {
  display: none;
}
.custom-check {
  width: 16px;
  height: 16px;
  border: 2px solid #d0d0d0;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}
.filter-option input:checked + .custom-check {
  background: #e94560;
  border-color: #e94560;
}
.filter-option input:checked + .custom-check::after {
  content: '';
  width: 5px;
  height: 9px;
  border: solid #fff;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
  margin-top: -1px;
}

/* ═══════════ Main ═══════════ */
.products-main {
  flex: 1;
  min-width: 0;
}

/* ═══════════ Toolbar ═══════════ */
.toolbar {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  background: #fff;
  padding: 0.8rem 1rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  border: 1px solid #e8e8e8;
}
.search-filter {
  flex: 1;
  min-width: 200px;
  position: relative;
}
.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #aaa;
  pointer-events: none;
}
.search-input {
  width: 100%;
  padding: 0.6rem 1rem 0.6rem 2.6rem;
  border: 1.5px solid #e0e0e0;
  font-size: 0.88rem;
  transition: all 0.2s;
  background: #fafafa;
}
.search-input:focus {
  outline: none;
  border-color: #111;
  background: #fff;
}
.sort-wrap {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  color: #888;
}
.sort-wrap select {
  padding: 0.6rem 1rem;
  border: 1.5px solid #e0e0e0;
  font-size: 0.88rem;
  background: #fafafa;
  cursor: pointer;
  transition: all 0.2s;
  color: #333;
}
.sort-wrap select:focus {
  outline: none;
  border-color: #111;
}
.result-count {
  font-size: 0.85rem;
  color: #888;
  white-space: nowrap;
}
.result-count strong {
  color: #1a1a2e;
}

/* ═══════════ Product Grid ═══════════ */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 0;
}
.product-card {
  background: #fff;
  overflow: hidden;
  transition: all 0.25s;
  text-decoration: none;
  color: inherit;
  display: block;
  border: 1px solid #e8e8e8;
  margin-left: -1px;
  margin-top: -1px;
}
.product-card:hover {
  z-index: 2;
  border-color: #111;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}
.product-image {
  position: relative;
  height: 240px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}
.product-card:hover .product-image img {
  transform: scale(1.05);
}
.placeholder-img {
  display: flex;
  align-items: center;
  justify-content: center;
}
.badge-brand {
  position: absolute;
  top: 0;
  left: 0;
  background: #111;
  color: #fff;
  padding: 0.3rem 0.7rem;
  font-size: 0.6rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 1px;
}
.product-info {
  padding: 1rem 1.1rem 1.2rem;
}
.product-name {
  font-size: 0.92rem;
  font-weight: 700;
  color: #111;
  margin-bottom: 0.2rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.product-meta {
  font-size: 0.65rem;
  color: #aaa;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  margin-bottom: 0.5rem;
}
.product-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  color: #e94560;
  font-weight: 800;
  font-size: 0.95rem;
}
.price-range {
  font-weight: 600;
  font-size: 0.8rem;
}
.no-price {
  color: #aaa;
  font-weight: 500;
}
.variant-count {
  font-size: 0.68rem;
  color: #888;
  background: #f0f0f0;
  padding: 0.2rem 0.5rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* ═══════════ Empty ═══════════ */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: #fff;
  border: 1px solid #e8e8e8;
}
.empty-state svg {
  margin-bottom: 1rem;
}
.empty-state h3 {
  color: #111;
  margin-bottom: 0.5rem;
  font-weight: 700;
}
.empty-state p {
  color: #999;
  margin-bottom: 1.5rem;
}
.btn-reset {
  padding: 0.6rem 1.8rem;
  background: #111;
  color: #fff;
  border: none;
  font-weight: 700;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.2s;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.btn-reset:hover {
  background: #e94560;
}

/* ═══════════ Responsive ═══════════ */
@media (max-width: 768px) {
  .products-container {
    flex-direction: column;
    margin-top: 1rem;
  }
  .filter-sidebar {
    width: 100%;
    position: static;
  }
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  }
  .product-image {
    height: 180px;
  }
  .toolbar {
    padding: 0.6rem;
  }
}
</style>
