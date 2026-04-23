<template>
  <div class="detail-page" v-if="product">
    <!-- Breadcrumb -->
    <div class="breadcrumb-bar">
      <div class="breadcrumb">
        <router-link to="/">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
          Trang chủ
        </router-link>
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m9 18 6-6-6-6"/></svg>
        <router-link to="/products">Sản phẩm</router-link>
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m9 18 6-6-6-6"/></svg>
        <span class="current">{{ product.tenSanPham }}</span>
      </div>
    </div>

    <div class="detail-container">
      <!-- Image Section -->
      <div class="detail-gallery">
        <div class="main-image-wrap">
          <button class="img-nav prev" @click="prevImage" v-if="allImages.length > 1">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M15 18l-6-6 6-6"/></svg>
          </button>
          <div class="main-image">
            <img v-if="selectedImage" :src="selectedImage" :alt="product.tenSanPham" />
            <div v-else class="placeholder-main">
              <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>
            </div>
            <div class="img-badge" v-if="product.trangThai === 1">
              <span class="badge-new">MỚI</span>
            </div>
          </div>
          <button class="img-nav next" @click="nextImage" v-if="allImages.length > 1">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M9 18l6-6-6-6"/></svg>
          </button>
        </div>
        <div class="thumb-list" v-if="allImages.length > 1">
          <div
            v-for="(img, idx) in allImages"
            :key="idx"
            class="thumb-item"
            :class="{ active: selectedImage === img }"
            @click="selectedImage = img"
          >
            <img :src="img" alt="" />
          </div>
        </div>
        <!-- Image counter -->
        <div class="img-counter" v-if="allImages.length > 1">
          {{ allImages.indexOf(selectedImage) + 1 }} / {{ allImages.length }}
        </div>
      </div>

      <!-- Info Section -->
      <div class="detail-info">
        <div class="info-top">
          <span class="brand-tag" v-if="product.thuongHieu">
            {{ product.thuongHieu.tenThuongHieu }}
          </span>
          <span class="info-divider" v-if="product.thuongHieu">·</span>
          <span class="origin-tag" v-if="product.xuatXu">{{ product.xuatXu.tenXuatXu }}</span>
        </div>

        <h1 class="product-title">{{ product.tenSanPham }}</h1>

        <!-- Rating + SKU row -->
        <div class="rating-row">
          <div class="stars">
            <svg v-for="i in 5" :key="i" width="15" height="15" viewBox="0 0 24 24"
              :fill="i<=4?'#f59e0b':'none'" :stroke="i<=4?'#f59e0b':'#e0e0e0'" stroke-width="2">
              <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
            </svg>
            <span class="rating-num">4.0</span>
            <span class="rating-sep">|</span>
            <span class="rating-count">128 đánh giá</span>
            <span class="rating-sep">|</span>
            <span class="sold-count">342 đã bán</span>
          </div>
        </div>

        <!-- Price block -->
        <div class="price-block" v-if="selectedDetail">
          <span class="price-main">{{ formatPrice(selectedDetail.giaTien) }}</span>
          <span class="price-sku">SKU: SP{{ String(selectedDetail.maSanPhamChiTiet).padStart(4,'0') }}</span>
        </div>
        <div class="price-block" v-else-if="minPrice != null">
          <span class="price-main">{{ formatPrice(minPrice) }}</span>
          <span class="price-range-label" v-if="maxPrice !== minPrice">~ {{ formatPrice(maxPrice) }}</span>
          <span class="price-hint">Chọn phân loại để xem giá</span>
        </div>
        <div class="price-block muted" v-else>
          <span class="no-price">Liên hệ để biết giá</span>
        </div>

        <!-- Material chip -->
        <div class="meta-row" v-if="product.chatLieu">
          <span class="meta-label">Chất liệu</span>
          <span class="meta-val">{{ product.chatLieu.tenChatLieu }}</span>
        </div>

        <div class="section-divider"></div>

        <!-- Color Selection -->
        <div class="variant-section" v-if="availableColors.length > 0">
          <div class="variant-header">
            <span class="variant-label">Màu sắc</span>
            <span class="variant-selected" v-if="selectedColor">
              — {{ availableColors.find(c=>c.maMauSac===selectedColor)?.tenMau }}
            </span>
          </div>
          <div class="variant-options">
            <button
              v-for="c in availableColors"
              :key="c.maMauSac"
              class="variant-btn"
              :class="{ active: selectedColor === c.maMauSac }"
              @click="selectColor(c.maMauSac)"
            >{{ c.tenMau }}</button>
          </div>
        </div>

        <!-- Size Selection -->
        <div class="variant-section" v-if="availableSizes.length > 0">
          <div class="variant-header">
            <span class="variant-label">Kích thước</span>
            <span class="variant-selected" v-if="selectedSize">
              — {{ availableSizes.find(s=>s.maSize===selectedSize)?.tenSize }}
            </span>
          </div>
          <div class="variant-options">
            <button
              v-for="s in availableSizes"
              :key="s.maSize"
              class="variant-btn size-btn"
              :class="{ active: selectedSize === s.maSize, disabled: !isSizeAvailable(s.maSize) }"
              :disabled="!isSizeAvailable(s.maSize)"
              @click="selectSize(s.maSize)"
            >{{ s.tenSize }}</button>
          </div>
        </div>

        <!-- Stock -->
        <div class="stock-row" v-if="selectedDetail">
          <div class="stock-in" v-if="selectedDetail.soLuong > 0">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
            Còn {{ selectedDetail.soLuong }} sản phẩm
          </div>
          <div class="stock-out" v-else>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><path d="M18 6 6 18M6 6l12 12"/></svg>
            Hết hàng
          </div>
        </div>

        <!-- Add to Cart -->
        <div class="cart-row">
          <div class="qty-control" v-if="selectedDetail && selectedDetail.soLuong > 0">
            <button @click="qty = Math.max(1, qty - 1)">−</button>
            <input type="number" :value="qty" @change="qty = Math.min(selectedDetail.soLuong, Math.max(1, +$event.target.value))" min="1" :max="selectedDetail.soLuong" />
            <button @click="qty = Math.min(selectedDetail.soLuong, qty + 1)">+</button>
          </div>
          <button class="btn-add-cart" :disabled="!selectedDetail || selectedDetail.soLuong <= 0" @click="addToCart">
            <svg width="19" height="19" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg>
            {{ !selectedDetail ? 'Chọn màu & size' : selectedDetail.soLuong <= 0 ? 'Hết hàng' : 'Thêm vào giỏ hàng' }}
          </button>
        </div>
        <div class="cart-toast" :class="{ show: cartMsg }">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="m9 12 2 2 4-4"/><circle cx="12" cy="12" r="10"/></svg>
          {{ cartMsg }}
        </div>

        <!-- Perks -->
        <div class="perks-row">
          <div class="perk-item">
            <div class="perk-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg></div>
            <div><strong>Free ship</strong><span>Đơn từ 500K</span></div>
          </div>
          <div class="perk-item">
            <div class="perk-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 1 0 .49-4"/></svg></div>
            <div><strong>Đổi trả 30 ngày</strong><span>Hoàn tiền 100%</span></div>
          </div>
          <div class="perk-item">
            <div class="perk-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg></div>
            <div><strong>Chính hãng 100%</strong><span>Bảo hành toàn quốc</span></div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tabs Section -->
    <div class="tabs-section">
      <div class="tabs-header">
        <button class="tab-btn" :class="{ active: activeTab === 'desc' }" @click="activeTab = 'desc'">Mô tả sản phẩm</button>
        <button class="tab-btn" :class="{ active: activeTab === 'variants' }" @click="activeTab = 'variants'">Biến thể ({{ activeDetails.length }})</button>
        <button class="tab-btn" :class="{ active: activeTab === 'ship' }" @click="activeTab = 'ship'">Vận chuyển & Đổi trả</button>
      </div>
      <div class="tab-content">
        <!-- Tab: Mô tả -->
        <div v-if="activeTab === 'desc'">
          <p v-if="product.moTa" class="desc-text">{{ product.moTa }}</p>
          <p v-else class="desc-empty">Chưa có mô tả cho sản phẩm này.</p>
          <div class="spec-table" v-if="product.chatLieu || product.xuatXu || product.thuongHieu">
            <div class="spec-row" v-if="product.thuongHieu"><span>Thương hiệu</span><span>{{ product.thuongHieu.tenThuongHieu }}</span></div>
            <div class="spec-row" v-if="product.chatLieu"><span>Chất liệu</span><span>{{ product.chatLieu.tenChatLieu }}</span></div>
            <div class="spec-row" v-if="product.xuatXu"><span>Xuất xứ</span><span>{{ product.xuatXu.tenXuatXu }}</span></div>
            <div class="spec-row"><span>Tình trạng</span><span class="text-success">Còn hàng</span></div>
          </div>
        </div>
        <!-- Tab: Biến thể -->
        <div v-if="activeTab === 'variants'">
          <div class="variants-table-wrap">
            <table class="variants-table">
              <thead><tr><th>Hình</th><th>Màu</th><th>Size</th><th>Giá</th><th>Kho</th></tr></thead>
              <tbody>
                <tr v-for="d in activeDetails" :key="d.maSanPhamChiTiet"
                    :class="{ 'selected-row': selectedDetail?.maSanPhamChiTiet === d.maSanPhamChiTiet }"
                    @click="selectVariant(d)">
                  <td><img v-if="d.hinhAnh" :src="d.hinhAnh" class="var-thumb" /><span v-else>—</span></td>
                  <td>{{ d.mauSac?.tenMau || '—' }}</td>
                  <td>{{ d.size?.tenSize || '—' }}</td>
                  <td class="td-price">{{ formatPrice(d.giaTien) }}</td>
                  <td><span :class="d.soLuong > 0 ? 'stock-ok' : 'stock-out-text'">{{ d.soLuong > 0 ? d.soLuong : 'Hết' }}</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Tab: Vận chuyển -->
        <div v-if="activeTab === 'ship'" class="ship-tab">
          <div class="ship-item">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>
            <div><strong>Miễn phí vận chuyển toàn quốc</strong><p>Áp dụng cho đơn hàng từ 500.000đ trở lên. Giao hàng từ 2-5 ngày làm việc.</p></div>
          </div>
          <div class="ship-item">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 1 0 .49-4"/></svg>
            <div><strong>Đổi trả trong 30 ngày</strong><p>Sản phẩm còn nguyên tem, chưa qua sử dụng. Hoàn tiền 100% nếu lỗi nhà sản xuất.</p></div>
          </div>
          <div class="ship-item">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
            <div><strong>Bảo hành chính hãng</strong><p>Tất cả sản phẩm đều có tem bảo hành và hóa đơn mua hàng rõ ràng.</p></div>
          </div>
        </div>
      </div>
    </div>

    <!-- Featured Products Slider -->
    <div class="featured-section" v-if="featuredProducts.length > 0">
      <div class="featured-header">
        <div class="featured-title">
          <span class="title-accent"></span>
          <div>
            <h2>Có thể bạn thích</h2>
            <p>Các sản phẩm tương tự dành cho bạn</p>
          </div>
        </div>
        <div class="slider-nav">
          <button class="nav-btn" @click="scrollSlider(-1)" :disabled="sliderStart">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M15 18l-6-6 6-6"/></svg>
          </button>
          <button class="nav-btn" @click="scrollSlider(1)" :disabled="sliderEnd">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M9 18l6-6-6-6"/></svg>
          </button>
        </div>
      </div>
      <div class="slider-track" ref="sliderRef" @scroll="updateSliderState">
        <router-link
          v-for="p in featuredProducts"
          :key="p.maSanPham"
          :to="`/product/${p.maSanPham}`"
          class="feat-card"
          @click="scrollToTop"
        >
          <div class="feat-img">
            <img v-if="p.hinhAnh" :src="p.hinhAnh" :alt="p.tenSanPham" />
            <div v-else class="feat-placeholder">
              <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>
            </div>
            <div class="feat-overlay">
              <span class="feat-quick-view">Xem nhanh →</span>
            </div>
          </div>
          <div class="feat-info">
            <span class="feat-brand" v-if="p.thuongHieu">{{ p.thuongHieu.tenThuongHieu }}</span>
            <p class="feat-name">{{ p.tenSanPham }}</p>
            <div class="feat-bottom">
              <span class="feat-price" v-if="getMinPrice(p)">{{ formatPrice(getMinPrice(p)) }}</span>
              <span class="feat-price muted" v-else>Liên hệ</span>
              <span class="feat-stars">★★★★</span>
            </div>
          </div>
        </router-link>
      </div>
    </div>
  </div>

  <!-- Loading -->
  <div class="loading" v-else-if="loading">
    <div class="spinner"></div>
    <p>Đang tải sản phẩm...</p>
  </div>

  <!-- Not Found -->
  <div class="not-found" v-else>
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><path d="M16 16s-1.5-2-4-2-4 2-4 2"/><line x1="9" y1="9" x2="9.01" y2="9"/><line x1="15" y1="9" x2="15.01" y2="9"/></svg>
    <h2>Không tìm thấy sản phẩm</h2>
    <router-link to="/products" class="btn-back">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5"/><path d="m12 19-7-7 7-7"/></svg>
      Quay lại danh sách
    </router-link>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const API = 'http://localhost:8080/api'

const product = ref(null)
const details = ref([])
const loading = ref(true)
const selectedColor = ref(null)
const selectedSize = ref(null)
const selectedImage = ref(null)
const qty = ref(1)
const cartMsg = ref('')
const activeTab = ref('desc')
const featuredProducts = ref([])
const sliderRef = ref(null)
const sliderStart = ref(true)
const sliderEnd = ref(false)
const allProductDetails = ref({})

function formatPrice(v) {
  if (v == null) return ''
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const activeDetails = computed(() => details.value.filter(d => d.trangThai === 1))

const allImages = computed(() => {
  const imgs = []
  if (product.value?.hinhAnh) imgs.push(product.value.hinhAnh)
  activeDetails.value.forEach(d => {
    if (d.hinhAnh && !imgs.includes(d.hinhAnh)) imgs.push(d.hinhAnh)
  })
  return imgs
})

const minPrice = computed(() => {
  const prices = activeDetails.value.map(d => d.giaTien).filter(g => g != null)
  return prices.length ? Math.min(...prices) : null
})

const maxPrice = computed(() => {
  const prices = activeDetails.value.map(d => d.giaTien).filter(g => g != null)
  return prices.length ? Math.max(...prices) : null
})

const availableColors = computed(() => {
  const map = new Map()
  activeDetails.value.forEach(d => {
    if (d.mauSac && !map.has(d.mauSac.maMauSac)) {
      map.set(d.mauSac.maMauSac, d.mauSac)
    }
  })
  return [...map.values()]
})

const availableSizes = computed(() => {
  const map = new Map()
  let source = activeDetails.value
  if (selectedColor.value) {
    source = source.filter(d => d.mauSac?.maMauSac === selectedColor.value)
  }
  source.forEach(d => {
    if (d.size && !map.has(d.size.maSize)) {
      map.set(d.size.maSize, d.size)
    }
  })
  return [...map.values()].sort((a, b) => {
    const na = parseFloat(a.tenSize) || 0
    const nb = parseFloat(b.tenSize) || 0
    return na - nb
  })
})

function isSizeAvailable(sizeId) {
  let source = activeDetails.value
  if (selectedColor.value) {
    source = source.filter(d => d.mauSac?.maMauSac === selectedColor.value)
  }
  return source.some(d => d.size?.maSize === sizeId && d.soLuong > 0)
}

const selectedDetail = computed(() => {
  if (!selectedColor.value || !selectedSize.value) return null
  return activeDetails.value.find(
    d => d.mauSac?.maMauSac === selectedColor.value && d.size?.maSize === selectedSize.value
  ) || null
})

function selectColor(id) {
  selectedColor.value = selectedColor.value === id ? null : id
  selectedSize.value = null
  qty.value = 1
  const detail = activeDetails.value.find(d => d.mauSac?.maMauSac === id && d.hinhAnh)
  if (detail) selectedImage.value = detail.hinhAnh
}

function selectSize(id) {
  selectedSize.value = selectedSize.value === id ? null : id
}

function prevImage() {
  const imgs = allImages.value
  const idx = imgs.indexOf(selectedImage.value)
  selectedImage.value = imgs[(idx - 1 + imgs.length) % imgs.length]
}

function nextImage() {
  const imgs = allImages.value
  const idx = imgs.indexOf(selectedImage.value)
  selectedImage.value = imgs[(idx + 1) % imgs.length]
}

function selectVariant(d) {
  selectedColor.value = d.mauSac?.maMauSac || null
  selectedSize.value = d.size?.maSize || null
  if (d.hinhAnh) selectedImage.value = d.hinhAnh
  qty.value = 1
}

function addToCart() {
  if (!selectedDetail.value || selectedDetail.value.soLuong <= 0) return
  const d = selectedDetail.value
  const item = {
    maSanPhamChiTiet: d.maSanPhamChiTiet,
    tenSanPham: product.value.tenSanPham,
    mauSac: d.mauSac?.tenMau || '',
    size: d.size?.tenSize || '',
    giaTien: d.giaTien,
    soLuong: qty.value,
    hinhAnh: d.hinhAnh || product.value.hinhAnh || '',
  }
  const saved = localStorage.getItem('cart')
  const cart = saved ? JSON.parse(saved) : []
  const existing = cart.find(c => c.maSanPhamChiTiet === item.maSanPhamChiTiet)
  if (existing) {
    existing.soLuong += qty.value
  } else {
    cart.push(item)
  }
  localStorage.setItem('cart', JSON.stringify(cart))
  window.dispatchEvent(new Event('cart-changed'))
  cartMsg.value = '✓ Đã thêm vào giỏ hàng!'
  setTimeout(() => { cartMsg.value = '' }, 2500)
}

function getMinPrice(p) {
  const dList = allProductDetails.value[p.maSanPham] || []
  const prices = dList.filter(d => d.trangThai === 1).map(d => d.giaTien).filter(g => g != null)
  return prices.length ? Math.min(...prices) : null
}

function scrollSlider(dir) {
  if (!sliderRef.value) return
  sliderRef.value.scrollBy({ left: dir * 300, behavior: 'smooth' })
}

function updateSliderState() {
  if (!sliderRef.value) return
  const el = sliderRef.value
  sliderStart.value = el.scrollLeft <= 10
  sliderEnd.value = el.scrollLeft + el.clientWidth >= el.scrollWidth - 10
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(async () => {
  const id = route.params.id
  try {
    const [pRes, dRes] = await Promise.all([
      fetch(`${API}/products/${id}`),
      fetch(`${API}/product-details/by-product/${id}`),
    ])
    if (pRes.ok) {
      product.value = await pRes.json()
    }
    if (dRes.ok) {
      details.value = await dRes.json()
      const firstImg = details.value.find(d => d.hinhAnh)
      if (firstImg) {
        selectedImage.value = firstImg.hinhAnh
      } else if (product.value?.hinhAnh) {
        selectedImage.value = product.value.hinhAnh
      }
    }
    // Load featured products
    const fpRes = await fetch(`${API}/products?page=0&size=12`)
    if (fpRes.ok) {
      const data = await fpRes.json()
      const list = Array.isArray(data) ? data : (data.content || [])
      featuredProducts.value = list.filter(p => p.maSanPham !== Number(id) && p.trangThai === 1).slice(0, 10)
      // Load details for price display
      await Promise.all(featuredProducts.value.map(async p => {
        try {
          const r = await fetch(`${API}/product-details/by-product/${p.maSanPham}`)
          if (r.ok) allProductDetails.value[p.maSanPham] = await r.json()
        } catch {}
      }))
    }
  } catch (e) {
    console.error('Lỗi tải chi tiết sản phẩm:', e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
/* ═══════════ Base ═══════════ */
.detail-page {
  background: #f7f8fa;
  min-height: 100vh;
}

/* ═══════════ Breadcrumb ═══════════ */
.breadcrumb-bar {
  background: #fff;
  border-bottom: 1px solid #eee;
}
.breadcrumb {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0.85rem 2rem;
  font-size: 0.82rem;
  color: #aaa;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.breadcrumb a {
  color: #777;
  text-decoration: none;
  transition: color 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
}
.breadcrumb a:hover { color: #e94560; }
.breadcrumb .current {
  color: #1a1a2e;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 280px;
}

/* ═══════════ Detail Container ═══════════ */
.detail-container {
  display: grid;
  grid-template-columns: 52% 48%;
  gap: 0;
  max-width: 1280px;
  margin: 2rem auto;
  padding: 0 2rem;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 2px 20px rgba(0,0,0,0.06);
  overflow: hidden;
}

/* ═══════════ Gallery ═══════════ */
.detail-gallery {
  position: sticky;
  top: 80px;
  align-self: flex-start;
  padding: 2rem;
  background: #fff;
}
.main-image-wrap {
  position: relative;
  width: 100%;
  aspect-ratio: 1;
  background: #f5f6f8;
  border-radius: 16px;
  overflow: hidden;
}
.main-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}
.main-image-wrap:hover .main-image img {
  transform: scale(1.03);
}
.placeholder-main {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}
.img-badge {
  position: absolute;
  top: 14px;
  left: 14px;
}
.badge-new {
  background: #e94560;
  color: #fff;
  font-size: 0.68rem;
  font-weight: 800;
  padding: 0.3rem 0.7rem;
  border-radius: 6px;
  letter-spacing: 1px;
}
.img-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: rgba(255,255,255,0.92);
  box-shadow: 0 2px 12px rgba(0,0,0,0.12);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  z-index: 2;
  color: #333;
}
.img-nav:hover { background: #fff; box-shadow: 0 4px 16px rgba(0,0,0,0.18); }
.img-nav.prev { left: 12px; }
.img-nav.next { right: 12px; }
.img-counter {
  text-align: center;
  font-size: 0.78rem;
  color: #aaa;
  margin-top: 0.7rem;
  font-weight: 500;
}
.thumb-list {
  display: flex;
  gap: 0.6rem;
  margin-top: 1rem;
  overflow-x: auto;
  padding-bottom: 4px;
  scrollbar-width: none;
}
.thumb-list::-webkit-scrollbar { display: none; }
.thumb-item {
  width: 66px;
  height: 66px;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  border: 2.5px solid transparent;
  flex-shrink: 0;
  transition: all 0.2s;
  background: #f5f6f8;
}
.thumb-item:hover { border-color: #ddd; }
.thumb-item.active { border-color: #e94560; box-shadow: 0 0 0 3px rgba(233,69,96,0.12); }
.thumb-item img { width: 100%; height: 100%; object-fit: cover; }

/* ═══════════ Info ═══════════ */
.detail-info {
  padding: 2rem 2.5rem 2rem 2rem;
  border-left: 1px solid #f0f0f0;
}
.info-top {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}
.brand-tag {
  background: #1a1a2e;
  color: #fff;
  padding: 0.25rem 0.8rem;
  border-radius: 5px;
  font-size: 0.7rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.8px;
}
.info-divider { color: #ddd; font-size: 0.85rem; }
.origin-tag {
  font-size: 0.8rem;
  color: #888;
  font-weight: 500;
}
.product-title {
  font-size: 1.65rem;
  font-weight: 800;
  color: #1a1a2e;
  margin-bottom: 0.8rem;
  line-height: 1.3;
  letter-spacing: -0.3px;
}

/* Rating */
.rating-row {
  display: flex;
  align-items: center;
  gap: 0;
  margin-bottom: 1.2rem;
  padding-bottom: 1.2rem;
  border-bottom: 1px solid #f0f0f0;
}
.stars {
  display: flex;
  align-items: center;
  gap: 2px;
  flex-wrap: wrap;
}
.rating-num {
  font-size: 0.85rem;
  font-weight: 700;
  color: #f59e0b;
  margin-left: 0.4rem;
}
.rating-sep { color: #e0e0e0; margin: 0 0.5rem; font-size: 0.85rem; }
.rating-count { font-size: 0.8rem; color: #888; }
.sold-count { font-size: 0.8rem; color: #888; }

/* Price block */
.price-block {
  background: linear-gradient(135deg, #fff5f7 0%, #fff8f9 100%);
  border: 1px solid #fde8ed;
  border-radius: 12px;
  padding: 1rem 1.4rem;
  margin-bottom: 1.2rem;
  display: flex;
  align-items: baseline;
  flex-wrap: wrap;
  gap: 0.6rem;
}
.price-block.muted {
  background: #f8f8f8;
  border-color: #eee;
}
.price-main {
  font-size: 1.9rem;
  font-weight: 900;
  color: #e94560;
  letter-spacing: -0.5px;
}
.price-range-label {
  font-size: 1.1rem;
  color: #e94560;
  font-weight: 600;
}
.price-hint {
  font-size: 0.78rem;
  color: #aaa;
  margin-left: auto;
  align-self: center;
}
.price-sku {
  font-size: 0.75rem;
  color: #bbb;
  font-family: monospace;
  margin-left: auto;
  align-self: center;
  background: #fff;
  padding: 0.2rem 0.6rem;
  border-radius: 5px;
  border: 1px solid #eee;
}
.no-price { color: #999; font-size: 1rem; }

/* Meta */
.meta-row {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-bottom: 1rem;
  font-size: 0.85rem;
}
.meta-label {
  color: #aaa;
  font-weight: 500;
  min-width: 80px;
}
.meta-val {
  color: #444;
  font-weight: 600;
  background: #f5f5f5;
  padding: 0.2rem 0.7rem;
  border-radius: 6px;
}
.section-divider {
  height: 1px;
  background: #f0f0f0;
  margin: 1rem 0;
}

/* Variant Selection */
.variant-section { margin-bottom: 1.2rem; }
.variant-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.65rem;
}
.variant-label {
  font-size: 0.82rem;
  font-weight: 700;
  color: #555;
  text-transform: uppercase;
  letter-spacing: 0.4px;
}
.variant-selected {
  font-size: 0.82rem;
  color: #e94560;
  font-weight: 600;
}
.variant-options { display: flex; flex-wrap: wrap; gap: 0.45rem; }
.variant-btn {
  padding: 0.45rem 1.1rem;
  border: 1.5px solid #e0e0e0;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  font-size: 0.83rem;
  font-weight: 600;
  transition: all 0.18s;
  color: #333;
}
.variant-btn:hover:not(.disabled) { border-color: #e94560; color: #e94560; background: #fff5f7; }
.variant-btn.active { border-color: #e94560; background: #e94560; color: #fff; box-shadow: 0 2px 8px rgba(233,69,96,0.25); }
.variant-btn.disabled { opacity: 0.3; cursor: not-allowed; text-decoration: line-through; }
.size-btn { min-width: 52px; text-align: center; }

/* Stock */
.stock-row {
  margin-bottom: 1rem;
}
.stock-in {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.82rem;
  font-weight: 600;
  color: #16a34a;
  background: #f0fdf4;
  border: 1px solid #bbf7d0;
  padding: 0.35rem 0.9rem;
  border-radius: 20px;
}
.stock-out {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.82rem;
  font-weight: 600;
  color: #dc2626;
  background: #fef2f2;
  border: 1px solid #fecaca;
  padding: 0.35rem 0.9rem;
  border-radius: 20px;
}

/* Cart Row */
.cart-row {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  margin-bottom: 0.6rem;
  padding: 1.2rem 0;
  border-top: 1px solid #f0f0f0;
}
.qty-control {
  display: flex;
  align-items: center;
  border: 1.5px solid #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  background: #fff;
}
.qty-control button {
  width: 38px;
  height: 44px;
  border: none;
  background: transparent;
  font-size: 1.2rem;
  cursor: pointer;
  color: #555;
  transition: background 0.15s;
  font-weight: 600;
  flex-shrink: 0;
}
.qty-control button:hover { background: #f5f5f5; color: #e94560; }
.qty-control input {
  width: 52px;
  height: 44px;
  border: none;
  border-left: 1.5px solid #e0e0e0;
  border-right: 1.5px solid #e0e0e0;
  text-align: center;
  font-size: 0.95rem;
  font-weight: 700;
  color: #1a1a2e;
  outline: none;
  background: #fff;
  appearance: textfield;
  -moz-appearance: textfield;
}
.qty-control input::-webkit-outer-spin-button,
.qty-control input::-webkit-inner-spin-button { -webkit-appearance: none; }
.btn-add-cart {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.55rem;
  padding: 0 1.5rem;
  height: 48px;
  background: linear-gradient(135deg, #e94560, #c23152);
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 0.92rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  letter-spacing: 0.2px;
  box-shadow: 0 4px 14px rgba(233,69,96,0.3);
}
.btn-add-cart:hover:not(:disabled) {
  background: linear-gradient(135deg, #d63851, #b02d48);
  box-shadow: 0 6px 18px rgba(233,69,96,0.4);
  transform: translateY(-1px);
}
.btn-add-cart:disabled { background: #d0d0d0; box-shadow: none; cursor: not-allowed; }
.cart-toast {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  font-weight: 600;
  color: #16a34a;
  opacity: 0;
  transition: opacity 0.3s;
  min-height: 24px;
  margin-bottom: 0.3rem;
}
.cart-toast.show { opacity: 1; }

/* Perks */
.perks-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.6rem;
  margin-top: 1.2rem;
  padding-top: 1.2rem;
  border-top: 1px solid #f0f0f0;
}
.perk-item {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  padding: 0.75rem 0.7rem;
  background: #f9f9f9;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  transition: border-color 0.2s;
}
.perk-item:hover { border-color: #fde8ed; background: #fff5f7; }
.perk-icon {
  width: 32px;
  height: 32px;
  background: #fff;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.perk-item strong {
  display: block;
  font-size: 0.73rem;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 1px;
  line-height: 1.2;
}
.perk-item span {
  font-size: 0.68rem;
  color: #aaa;
  line-height: 1.2;
}

/* ═══════════ Tabs Section ═══════════ */
.tabs-section {
  max-width: 1280px;
  margin: 1.5rem auto 3rem;
  padding: 0 2rem;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  overflow: hidden;
}
.tabs-header {
  display: flex;
  border-bottom: 1px solid #f0f0f0;
}
.tab-btn {
  padding: 1rem 2rem;
  border: none;
  background: none;
  font-size: 0.88rem;
  font-weight: 700;
  color: #999;
  cursor: pointer;
  border-bottom: 3px solid transparent;
  margin-bottom: -1px;
  transition: all 0.2s;
  letter-spacing: 0.1px;
}
.tab-btn:hover { color: #e94560; }
.tab-btn.active { color: #e94560; border-bottom-color: #e94560; background: #fff9fa; }
.tab-content { padding: 2rem; }
.desc-text {
  font-size: 0.93rem;
  color: #555;
  line-height: 1.85;
  margin-bottom: 2rem;
  max-width: 680px;
}
.desc-empty { color: #ccc; font-style: italic; font-size: 0.9rem; }
.spec-table {
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  overflow: hidden;
  max-width: 480px;
}
.spec-row {
  display: flex;
  border-bottom: 1px solid #f0f0f0;
}
.spec-row:last-child { border-bottom: none; }
.spec-row span:first-child {
  width: 150px;
  padding: 0.8rem 1.1rem;
  background: #f8f8f8;
  font-size: 0.83rem;
  font-weight: 700;
  color: #666;
  flex-shrink: 0;
  letter-spacing: 0.1px;
}
.spec-row span:last-child {
  padding: 0.8rem 1.1rem;
  font-size: 0.83rem;
  color: #333;
  font-weight: 500;
}
.text-success { color: #16a34a; font-weight: 700; }
.var-thumb {
  width: 42px;
  height: 42px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #eee;
}
.variants-table-wrap {
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  border: 1px solid #f0f0f0;
}
.variants-table {
  width: 100%;
  border-collapse: collapse;
}
.variants-table th {
  background: #1a1a2e;
  color: #fff;
  padding: 0.8rem 1rem;
  font-size: 0.78rem;
  font-weight: 700;
  text-align: left;
  letter-spacing: 0.3px;
}
.variants-table td {
  padding: 0.75rem 1rem;
  font-size: 0.84rem;
  border-bottom: 1px solid #f5f5f5;
}
.variants-table tbody tr { cursor: pointer; transition: background 0.12s; }
.variants-table tbody tr:hover { background: #f8f9fa; }
.variants-table tbody tr.selected-row { background: #fff5f7; }
.td-price { color: #e94560; font-weight: 800; }
.stock-ok { color: #16a34a; font-weight: 700; }
.stock-out-text { color: #dc2626; font-weight: 700; font-size: 0.8rem; }

/* Ship tab */
.ship-tab { display: flex; flex-direction: column; gap: 1rem; }
.ship-item {
  display: flex;
  gap: 1.2rem;
  padding: 1.3rem 1.5rem;
  background: #fafafa;
  border-radius: 14px;
  border: 1px solid #f0f0f0;
  transition: border-color 0.2s;
}
.ship-item:hover { border-color: #fde8ed; background: #fff9fa; }
.ship-item svg { flex-shrink: 0; margin-top: 2px; }
.ship-item strong { display: block; font-size: 0.92rem; font-weight: 800; color: #1a1a2e; margin-bottom: 5px; }
.ship-item p { font-size: 0.82rem; color: #888; margin: 0; line-height: 1.65; }

/* ═══════════ Featured Slider ═══════════ */
.featured-section {
  background: #fff;
  padding: 3rem 2rem 3.5rem;
  margin-top: 1.5rem;
  border-top: 1px solid #f0f0f0;
}
.featured-header {
  max-width: 1280px;
  margin: 0 auto 1.8rem;
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}
.featured-title {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.title-accent {
  width: 5px;
  height: 36px;
  background: linear-gradient(to bottom, #e94560, #c23152);
  border-radius: 4px;
  flex-shrink: 0;
}
.featured-title h2 {
  font-size: 1.4rem;
  font-weight: 900;
  color: #1a1a2e;
  margin: 0 0 2px;
  letter-spacing: -0.3px;
}
.featured-title p {
  font-size: 0.8rem;
  color: #aaa;
  margin: 0;
}
.slider-nav { display: flex; gap: 0.5rem; }
.nav-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1.5px solid #e0e0e0;
  background: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  color: #555;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06);
}
.nav-btn:hover:not(:disabled) { border-color: #e94560; color: #e94560; background: #fff5f7; }
.nav-btn:disabled { opacity: 0.25; cursor: not-allowed; }
.slider-track {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  gap: 1.2rem;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scrollbar-width: none;
  padding-bottom: 6px;
}
.slider-track::-webkit-scrollbar { display: none; }
.feat-card {
  flex-shrink: 0;
  width: 210px;
  scroll-snap-align: start;
  text-decoration: none;
  color: inherit;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  border: 1.5px solid #f0f0f0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  transition: all 0.25s ease;
}
.feat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0,0,0,0.1);
  border-color: #fde8ed;
}
.feat-img {
  position: relative;
  height: 190px;
  background: #f5f6f8;
  overflow: hidden;
}
.feat-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}
.feat-card:hover .feat-img img { transform: scale(1.06); }
.feat-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.feat-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.35) 0%, transparent 50%);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-bottom: 12px;
  opacity: 0;
  transition: opacity 0.25s;
}
.feat-card:hover .feat-overlay { opacity: 1; }
.feat-quick-view {
  color: #fff;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.3px;
  background: rgba(255,255,255,0.2);
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  border: 1px solid rgba(255,255,255,0.4);
  backdrop-filter: blur(4px);
}
.feat-info { padding: 0.9rem 1rem 1rem; }
.feat-brand {
  display: inline-block;
  font-size: 0.63rem;
  font-weight: 800;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.6px;
  margin-bottom: 0.3rem;
}
.feat-name {
  font-size: 0.84rem;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 0.6rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.45;
}
.feat-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.feat-price {
  font-size: 0.92rem;
  font-weight: 900;
  color: #e94560;
}
.feat-price.muted { color: #ccc; font-weight: 500; font-size: 0.8rem; }
.feat-stars { font-size: 0.72rem; color: #f59e0b; letter-spacing: 1px; }

/* ═══════════ Loading ═══════════ */
.loading {
  text-align: center;
  padding: 6rem 2rem;
  color: #bbb;
}
.spinner {
  width: 44px;
  height: 44px;
  border: 3px solid #f0f0f0;
  border-top-color: #e94560;
  border-radius: 50%;
  animation: spin 0.75s linear infinite;
  margin: 0 auto 1.2rem;
}
@keyframes spin { to { transform: rotate(360deg); } }

.not-found { text-align: center; padding: 6rem 2rem; }
.not-found svg { margin-bottom: 1.2rem; }
.not-found h2 { color: #666; margin-bottom: 1.2rem; font-weight: 700; }
.btn-back {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 2rem;
  background: #e94560;
  color: #fff;
  text-decoration: none;
  border-radius: 12px;
  font-weight: 700;
  transition: all 0.2s;
}
.btn-back:hover { background: #d63851; transform: translateY(-1px); }

/* ═══════════ Responsive ═══════════ */
@media (max-width: 900px) {
  .detail-container {
    grid-template-columns: 1fr;
    margin: 1rem auto;
  }
  .detail-gallery { position: static; }
  .detail-info { border-left: none; border-top: 1px solid #f0f0f0; }
}
@media (max-width: 640px) {
  .detail-container { padding: 0 1rem; border-radius: 12px; }
  .detail-gallery, .detail-info { padding: 1.5rem; }
  .product-title { font-size: 1.35rem; }
  .price-main { font-size: 1.6rem; }
  .perks-row { grid-template-columns: 1fr; gap: 0.5rem; }
  .tab-btn { padding: 0.75rem 1rem; font-size: 0.8rem; }
  .feat-card { width: 165px; }
  .feat-img { height: 150px; }
  .featured-section { padding: 2rem 1rem 2.5rem; }
  .tabs-section { padding: 0 1rem; border-radius: 12px; }
  .tab-content { padding: 1.5rem 1rem; }
  .breadcrumb .current { max-width: 150px; }
  .cart-row { flex-wrap: wrap; }
  .btn-add-cart { flex: 1 1 100%; }
}
</style>
