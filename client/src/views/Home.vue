<template>
  <div class="home">
    <!-- Hero Carousel / Slideshow -->
    <section class="hero-carousel">
      <div class="carousel-track" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
        <div
          v-for="(slide, idx) in slides"
          :key="idx"
          class="carousel-slide"
        >
          <img :src="slide.image" :alt="'Slide ' + (idx + 1)" class="slide-bg-img" />
          <div class="slide-cta">
            <router-link to="/products" class="btn-vintage">Xem thêm</router-link>
          </div>
        </div>
      </div>
      <!-- Indicators -->
      <div class="carousel-indicators">
        <button
          v-for="(slide, idx) in slides"
          :key="idx"
          class="indicator"
          :class="{ active: currentSlide === idx }"
          @click="goToSlide(idx)"
        ></button>
      </div>
      <!-- Nav arrows -->
      <button class="carousel-arrow arrow-left" @click="prevSlide">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
      </button>
      <button class="carousel-arrow arrow-right" @click="nextSlide">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m9 18 6-6-6-6"/></svg>
      </button>
    </section>

    <!-- Stats Ribbon -->
    <section class="stats-ribbon">
      <div class="stats-inner">
        <div class="stat-item">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          <div><strong>10,000+</strong><span>Khách hàng</span></div>
        </div>
        <div class="stat-item">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 3H8l-2 4h12l-2-4z"/></svg>
          <div><strong>500+</strong><span>Mẫu giày</span></div>
        </div>
        <div class="stat-item">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 2 L15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 Z"/></svg>
          <div><strong>4.9/5</strong><span>Đánh giá</span></div>
        </div>
        <div class="stat-item">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
          <div><strong>100%</strong><span>Chính hãng</span></div>
        </div>
      </div>
    </section>

    <!-- Featured Categories -->
    <section class="categories">
      <div class="section-header">
        <span class="section-tag">DANH MỤC</span>
        <h2 class="section-title">Khám Phá Theo Phong Cách</h2>
      </div>
      <div class="category-grid">
        <router-link to="/products" class="category-card" v-for="(cat, idx) in categories" :key="idx">
          <img :src="cat.image" :alt="cat.name" class="cat-img" />
          <div class="cat-overlay"></div>
          <div class="cat-content">
            <span class="cat-label">{{ cat.tag }}</span>
            <h3>{{ cat.name }}</h3>
            <span class="cat-cta">Khám phá →</span>
          </div>
        </router-link>
      </div>
    </section>

    <!-- Featured Products -->
    <section class="featured">
      <div class="section-header">
        <span class="section-tag">BÁN CHẠY</span>
        <h2 class="section-title">Sản Phẩm Nổi Bật</h2>
      </div>
      <div class="product-grid">
        <router-link
          v-for="product in featuredProducts"
          :key="product.maSanPham"
          :to="`/products/${product.maSanPham}`"
          class="product-card"
        >
          <div class="product-image">
            <img v-if="product.thumbImage" :src="product.thumbImage" :alt="product.tenSanPham" />
            <div v-else class="placeholder-img">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" opacity="0.2"><rect x="3" y="3" width="18" height="18" rx="0"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>
            </div>
            <span class="badge-brand" v-if="product.thuongHieu">{{ product.thuongHieu.tenThuongHieu }}</span>
          </div>
          <div class="product-info">
            <p class="product-meta" v-if="product.chatLieu">{{ product.chatLieu.tenChatLieu }}</p>
            <h4>{{ product.tenSanPham }}</h4>
            <div class="product-footer">
              <span class="price" v-if="product.minPrice != null">{{ formatPrice(product.minPrice) }}</span>
              <span class="price no-price" v-else>Liên hệ</span>
              <span class="variant-count" v-if="product.variantCount">{{ product.variantCount }} phân loại</span>
            </div>
          </div>
        </router-link>
      </div>
      <div class="see-all" v-if="featuredProducts.length > 0">
        <router-link to="/products" class="btn-see-all">
          XEM TẤT CẢ SẢN PHẨM
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
        </router-link>
      </div>
    </section>

    <!-- Promo Banner -->
    <section class="promo-banner">
      <div class="promo-inner">
        <div class="promo-text">
          <span class="promo-label">ƯU ĐÃI ĐẶC BIỆT</span>
          <h2>Giảm đến <strong>50%</strong> cho đơn hàng đầu tiên</h2>
          <p>Đăng ký tài khoản ngay hôm nay để nhận ưu đãi độc quyền</p>
          <router-link to="/register" class="btn-promo">ĐĂNG KÝ NGAY →</router-link>
        </div>
      </div>
    </section>

    <!-- Why Choose Us -->
    <section class="why-us">
      <div class="section-header">
        <span class="section-tag">CAM KẾT</span>
        <h2 class="section-title">Tại Sao Chọn Chúng Tôi?</h2>
      </div>
      <div class="features-grid">
        <div class="feature-card" v-for="(f, idx) in features" :key="idx">
          <div class="feature-icon" v-html="f.svg"></div>
          <div class="feature-body">
            <h4>{{ f.title }}</h4>
            <p>{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const API = 'http://localhost:8080/api'
const products = ref([])
const productDetails = ref([])

/* ── Carousel ── */
const currentSlide = ref(0)
let autoplayTimer = null

const slides = [
  { image: '/slides/slide1.jpg' },
  { image: '/slides/slide2.jpg' },
  { image: '/slides/slide3.jpg' },
]

const categories = [
  {
    name: 'Giày Thể Thao',
    tag: 'SPORT',
    image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&h=400&fit=crop',
  },
  {
    name: 'Giày Tây',
    tag: 'CLASSIC',
    image: 'https://images.unsplash.com/photo-1614252369475-531eba835eb1?w=600&h=400&fit=crop',
  },
  {
    name: 'Giày Cao Gót',
    tag: 'HEELS',
    image: 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=600&h=400&fit=crop',
  },
  {
    name: 'Giày Boots',
    tag: 'BOOTS',
    image: 'https://images.unsplash.com/photo-1608256246200-53e635b5b65f?w=600&h=400&fit=crop',
  },
]

const features = [
  {
    title: 'Miễn Phí Vận Chuyển',
    desc: 'Giao hàng miễn phí toàn quốc cho đơn hàng từ 500K',
    svg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>'
  },
  {
    title: 'Đổi Trả 30 Ngày',
    desc: 'Đổi trả miễn phí trong vòng 30 ngày nếu không hài lòng',
    svg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"/></svg>'
  },
  {
    title: 'Hàng Chính Hãng',
    desc: 'Cam kết 100% sản phẩm chính hãng, nguồn gốc rõ ràng',
    svg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="m9 12 2 2 4-4"/></svg>'
  },
  {
    title: 'Hỗ Trợ 24/7',
    desc: 'Đội ngũ tư vấn luôn sẵn sàng hỗ trợ bạn mọi lúc',
    svg: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>'
  },
]

function nextSlide() {
  currentSlide.value = (currentSlide.value + 1) % slides.length
  resetAutoplay()
}
function prevSlide() {
  currentSlide.value = (currentSlide.value - 1 + slides.length) % slides.length
  resetAutoplay()
}
function goToSlide(idx) {
  currentSlide.value = idx
  resetAutoplay()
}
function resetAutoplay() {
  clearInterval(autoplayTimer)
  autoplayTimer = setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % slides.length
  }, 5000)
}

/* ── Products ── */
function formatPrice(v) {
  if (v == null) return ''
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const featuredProducts = computed(() => {
  return products.value
    .filter(p => p.trangThai === 1)
    .slice(0, 8)
    .map(p => {
      const details = productDetails.value.filter(
        d => d.sanPham?.maSanPham === p.maSanPham && d.trangThai === 1
      )
      const prices = details.map(d => d.giaTien).filter(g => g != null)
      return {
        ...p,
        minPrice: prices.length ? Math.min(...prices) : null,
        thumbImage: p.hinhAnh || null,
        variantCount: details.length,
      }
    })
})

onMounted(async () => {
  autoplayTimer = setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % slides.length
  }, 5000)

  try {
    const [p, pd] = await Promise.all([
      fetch(`${API}/products`).then(r => r.json()),
      fetch(`${API}/product-details`).then(r => r.json()),
    ])
    products.value = p
    productDetails.value = pd
  } catch (e) {
    console.error(e)
  }
})

onUnmounted(() => {
  clearInterval(autoplayTimer)
})
</script>

<style scoped>
.home {
  min-height: 100vh;
}

/* ═══════════════════════════════════
   HERO CAROUSEL
   ═══════════════════════════════════ */
.hero-carousel {
  position: relative;
  overflow: hidden;
  height: 520px;
}
.carousel-track {
  display: flex;
  height: 100%;
  transition: transform 0.7s cubic-bezier(0.65, 0, 0.35, 1);
}
.carousel-slide {
  min-width: 100%;
  height: 100%;
  position: relative;
}
.slide-bg-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.slide-cta {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
}
.btn-vintage {
  display: inline-block;
  padding: 0.75rem 2.4rem;
  border: 2px solid #fff;
  color: #fff;
  background: rgba(0, 0, 0, 0.25);
  text-decoration: none;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  transition: all 0.3s ease;
}
.btn-vintage:hover {
  background: #fff;
  color: #222;
}

/* Carousel arrows */
.carousel-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255,255,255,0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255,255,255,0.2);
  color: #fff;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: all 0.3s;
}
.carousel-arrow:hover {
  background: rgba(255,255,255,0.3);
}
.arrow-left { left: 24px; }
.arrow-right { right: 24px; }

/* Carousel indicators */
.carousel-indicators {
  position: absolute;
  bottom: 28px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 10;
}
.indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,0.6);
  background: transparent;
  cursor: pointer;
  transition: all 0.3s;
  padding: 0;
}
.indicator.active {
  background: #e94560;
  border-color: #e94560;
  transform: scale(1.2);
  box-shadow: 0 0 12px rgba(233, 69, 96, 0.6);
}

/* ═══════════════════════════════════
   STATS RIBBON
   ═══════════════════════════════════ */
.stats-ribbon {
  background: #fff;
  border-bottom: 1px solid #eee;
  box-shadow: 0 2px 20px rgba(0,0,0,0.04);
}
.stats-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  padding: 1.5rem 2rem;
  gap: 1rem;
}
.stat-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  justify-content: center;
  color: #1a1a2e;
}
.stat-item svg {
  color: #e94560;
  flex-shrink: 0;
}
.stat-item div {
  display: flex;
  flex-direction: column;
}
.stat-item strong {
  font-size: 1.15rem;
  font-weight: 800;
}
.stat-item span {
  font-size: 0.8rem;
  color: #888;
  font-weight: 500;
}

/* ═══════════════════════════════════
   SECTION HEADER
   ═══════════════════════════════════ */
.section-header {
  margin-bottom: 2rem;
}
.section-tag {
  display: inline-block;
  font-size: 0.7rem;
  font-weight: 800;
  letter-spacing: 3px;
  color: #e94560;
  text-transform: uppercase;
  margin-bottom: 0.4rem;
}
.section-title {
  font-size: 1.6rem;
  font-weight: 800;
  color: #111;
  letter-spacing: -0.5px;
}

/* ═══════════════════════════════════
   CATEGORIES — image cards with overlay
   ═══════════════════════════════════ */
.categories {
  padding: 3rem 2rem;
  max-width: 1320px;
  margin: 0 auto;
}
.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0;
}
.category-card {
  position: relative;
  overflow: hidden;
  text-decoration: none;
  color: #fff;
  height: 280px;
  border: 1px solid #111;
  margin-left: -1px;
  display: block;
}
.category-card:first-child { margin-left: 0; }
.cat-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.category-card:hover .cat-img {
  transform: scale(1.08);
}
.cat-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.75) 0%, rgba(0,0,0,0.15) 50%, rgba(0,0,0,0.05) 100%);
  transition: background 0.4s;
}
.category-card:hover .cat-overlay {
  background: linear-gradient(to top, rgba(0,0,0,0.85) 0%, rgba(0,0,0,0.3) 50%, rgba(0,0,0,0.1) 100%);
}
.cat-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1.5rem;
  z-index: 2;
}
.cat-label {
  display: inline-block;
  font-size: 0.6rem;
  font-weight: 800;
  letter-spacing: 2.5px;
  color: #e94560;
  margin-bottom: 0.4rem;
}
.cat-content h3 {
  font-size: 1.15rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  line-height: 1.2;
}
.cat-cta {
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  opacity: 0;
  transform: translateY(8px);
  transition: all 0.3s ease;
}
.category-card:hover .cat-cta {
  opacity: 1;
  transform: translateY(0);
}

/* ═══════════════════════════════════
   FEATURED PRODUCTS — sharp grid
   ═══════════════════════════════════ */
.featured {
  padding: 3rem 2rem 4rem;
  max-width: 1320px;
  margin: 0 auto;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0;
}
.product-card {
  background: #fff;
  overflow: hidden;
  border: 1px solid #e8e8e8;
  margin-left: -1px;
  margin-top: -1px;
  transition: all 0.25s;
  cursor: pointer;
  text-decoration: none;
  color: inherit;
  display: block;
}
.product-card:hover {
  z-index: 2;
  border-color: #111;
  box-shadow: 0 8px 30px rgba(0,0,0,0.12);
}
.product-image {
  background: #f5f5f5;
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
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
.product-meta {
  font-size: 0.65rem;
  color: #aaa;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  margin-bottom: 0.3rem;
}
.product-info h4 {
  font-size: 0.92rem;
  font-weight: 700;
  margin-bottom: 0.6rem;
  color: #111;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.35;
}
.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 0.6rem;
  border-top: 1px solid #f0f0f0;
}
.price {
  color: #111;
  font-weight: 800;
  font-size: 1rem;
}
.no-price {
  color: #aaa;
  font-weight: 500;
}
.variant-count {
  font-size: 0.65rem;
  color: #888;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* See All */
.see-all {
  text-align: center;
  margin-top: 2.5rem;
}
.btn-see-all {
  display: inline-flex;
  align-items: center;
  gap: 0.6rem;
  padding: 0.85rem 2.5rem;
  border: 2px solid #111;
  color: #111;
  text-decoration: none;
  font-weight: 800;
  font-size: 0.75rem;
  letter-spacing: 2px;
  text-transform: uppercase;
  transition: all 0.25s;
}
.btn-see-all:hover {
  background: #111;
  color: #fff;
}
.btn-see-all svg {
  transition: transform 0.25s;
}
.btn-see-all:hover svg {
  transform: translateX(4px);
}

/* ═══════════════════════════════════
   PROMO BANNER — sharp, no circles
   ═══════════════════════════════════ */
.promo-banner {
  padding: 0 2rem;
  max-width: 1320px;
  margin: 0 auto 3rem;
}
.promo-inner {
  background: #111;
  padding: 3rem 3.5rem;
  display: flex;
  align-items: center;
}
.promo-text {
  max-width: 560px;
}
.promo-label {
  display: inline-block;
  padding: 0.3rem 0.8rem;
  background: #e94560;
  font-size: 0.65rem;
  font-weight: 800;
  color: #fff;
  letter-spacing: 2px;
  margin-bottom: 1rem;
}
.promo-text h2 {
  font-size: 1.8rem;
  font-weight: 800;
  color: #fff;
  margin-bottom: 0.6rem;
  line-height: 1.3;
}
.promo-text h2 strong {
  color: #e94560;
}
.promo-text p {
  color: rgba(255,255,255,0.5);
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
  line-height: 1.6;
}
.btn-promo {
  display: inline-block;
  padding: 0.75rem 2rem;
  background: transparent;
  border: 2px solid #fff;
  color: #fff;
  text-decoration: none;
  font-weight: 800;
  font-size: 0.75rem;
  letter-spacing: 2px;
  transition: all 0.25s;
}
.btn-promo:hover {
  background: #fff;
  color: #111;
}

/* ═══════════════════════════════════
   WHY CHOOSE US — icon cards, flat
   ═══════════════════════════════════ */
.why-us {
  padding: 3rem 2rem 4rem;
  max-width: 1320px;
  margin: 0 auto;
}
.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0;
}
.feature-card {
  padding: 2rem 1.5rem;
  border: 1px solid #e8e8e8;
  margin-left: -1px;
  transition: all 0.25s;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.feature-card:first-child { margin-left: 0; }
.feature-card:hover {
  z-index: 2;
  border-color: #111;
  background: #fafafa;
}
.feature-icon {
  width: 52px;
  height: 52px;
  background: #111;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: background 0.25s;
}
.feature-icon :deep(svg) {
  stroke: #fff;
}
.feature-card:hover .feature-icon {
  background: #e94560;
}
.feature-body h4 {
  font-size: 0.88rem;
  font-weight: 800;
  color: #111;
  margin-bottom: 0.3rem;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}
.feature-body p {
  font-size: 0.8rem;
  color: #888;
  line-height: 1.55;
  margin: 0;
}

/* ═══════════════════════════════════
   RESPONSIVE
   ═══════════════════════════════════ */
@media (max-width: 1024px) {
  .hero-carousel { height: 440px; }
  .category-card { height: 240px; }
  .promo-inner { padding: 2.5rem; }
  .promo-text h2 { font-size: 1.6rem; }
}
@media (max-width: 768px) {
  .hero-carousel { height: 400px; }
  .carousel-arrow { width: 40px; height: 40px; }
  .arrow-left { left: 12px; }
  .arrow-right { right: 12px; }
  .stats-inner {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
    padding: 1.2rem 1.5rem;
  }
  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .category-card {
    height: 220px;
    margin-top: -1px;
  }
  .category-card:nth-child(odd) { margin-left: 0; }
  .product-grid { grid-template-columns: repeat(2, 1fr); }
  .product-image { height: 180px; }
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .feature-card { margin-top: -1px; }
  .feature-card:nth-child(odd) { margin-left: 0; }
  .promo-inner { padding: 2rem 1.5rem; }
  .promo-text h2 { font-size: 1.3rem; }
  .btn-vintage { padding: 0.6rem 1.8rem; font-size: 0.9rem; }
}
@media (max-width: 480px) {
  .hero-carousel { height: 300px; }
  .btn-vintage { padding: 0.5rem 1.4rem; font-size: 0.8rem; letter-spacing: 1px; }
  .stats-inner { grid-template-columns: 1fr 1fr; gap: 0.8rem; }
  .stat-item svg { display: none; }
  .category-grid { grid-template-columns: 1fr; }
  .category-card {
    height: 200px;
    margin-left: 0;
  }
  .product-grid { grid-template-columns: 1fr; }
  .product-image { height: 220px; }
  .features-grid { grid-template-columns: 1fr; }
  .feature-card { margin-left: 0; }
}
</style>
