<template>
  <div class="cart-page">
    <!-- Banner -->
    <div class="cart-banner">
      <div class="banner-inner">
        <h1>Giỏ hàng</h1>
        <p>{{ cartItems.length }} sản phẩm trong giỏ hàng của bạn</p>
      </div>
    </div>

    <div class="cart-container">
      <!-- Empty cart -->
      <div v-if="cartItems.length === 0" class="empty-cart">
        <svg width="72" height="72" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.2">
          <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
          <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
        </svg>
        <h2>Giỏ hàng trống</h2>
        <p>Hãy thêm sản phẩm yêu thích vào giỏ hàng</p>
        <router-link to="/products" class="btn-continue">Tiếp tục mua sắm</router-link>
      </div>

      <!-- Cart with items -->
      <div v-else class="cart-grid">
        <div class="cart-items">
          <!-- Header -->
          <div class="items-header">
            <span class="col-product">Sản phẩm</span>
            <span class="col-price">Đơn giá</span>
            <span class="col-qty">Số lượng</span>
            <span class="col-total">Thành tiền</span>
            <span class="col-action"></span>
          </div>

          <!-- Items -->
          <div class="cart-item" v-for="(item, idx) in cartItems" :key="idx">
            <div class="col-product">
              <div class="item-image">
                <img v-if="item.hinhAnh" :src="item.hinhAnh" :alt="item.tenSanPham" />
                <div v-else class="img-placeholder">
                  <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1"><rect x="3" y="3" width="18" height="18" rx="0"/></svg>
                </div>
              </div>
              <div class="item-info">
                <h4>{{ item.tenSanPham }}</h4>
                <div class="item-variant">
                  <span v-if="item.mauSac">Màu: {{ item.mauSac }}</span>
                  <span v-if="item.size">Size: {{ item.size }}</span>
                </div>
              </div>
            </div>
            <div class="col-price">{{ formatPrice(item.giaTien) }}</div>
            <div class="col-qty">
              <div class="qty-control">
                <button @click="changeQty(idx, -1)" :disabled="item.soLuong <= 1">−</button>
                <span>{{ item.soLuong }}</span>
                <button @click="changeQty(idx, 1)">+</button>
              </div>
            </div>
            <div class="col-total fw700">{{ formatPrice(item.giaTien * item.soLuong) }}</div>
            <div class="col-action">
              <button class="btn-remove" @click="removeItem(idx)" title="Xóa">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
              </button>
            </div>
          </div>
        </div>

        <!-- Summary -->
        <div class="cart-summary">
          <div class="summary-card">
            <h3>Tóm tắt đơn hàng</h3>
            <div class="summary-row">
              <span>Tạm tính ({{ totalQty }} sản phẩm)</span>
              <span>{{ formatPrice(subtotal) }}</span>
            </div>
            <div class="summary-row">
              <span>Phí vận chuyển</span>
              <span class="free-ship">Miễn phí</span>
            </div>
            <div class="summary-divider"></div>
            <div class="summary-row total">
              <span>Tổng cộng</span>
              <span class="total-price">{{ formatPrice(subtotal) }}</span>
            </div>
            <router-link to="/checkout" class="btn-checkout">
              Tiến hành đặt hàng
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
            </router-link>
            <router-link to="/products" class="btn-back-shop">← Tiếp tục mua sắm</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const cartItems = ref([])

function loadCart() {
  const saved = localStorage.getItem('cart')
  cartItems.value = saved ? JSON.parse(saved) : []
}

function saveCart() {
  localStorage.setItem('cart', JSON.stringify(cartItems.value))
  window.dispatchEvent(new Event('cart-changed'))
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const subtotal = computed(() => cartItems.value.reduce((s, i) => s + (i.giaTien || 0) * (i.soLuong || 0), 0))
const totalQty = computed(() => cartItems.value.reduce((s, i) => s + (i.soLuong || 0), 0))

function changeQty(idx, delta) {
  const item = cartItems.value[idx]
  item.soLuong = Math.max(1, item.soLuong + delta)
  saveCart()
}

function removeItem(idx) {
  cartItems.value.splice(idx, 1)
  saveCart()
}

onMounted(() => {
  loadCart()
  window.addEventListener('cart-changed', loadCart)
})
</script>

<style scoped>
.cart-page { min-height: 80vh; }

.cart-banner {
  background: #111;
  color: #fff;
  padding: 3rem 2rem 2.5rem;
}
.banner-inner {
  max-width: 1320px;
  margin: 0 auto;
}
.banner-inner h1 { font-size: 1.8rem; font-weight: 800; margin: 0 0 0.3rem; }
.banner-inner p { font-size: 0.9rem; color: #aaa; margin: 0; }

.cart-container {
  max-width: 1320px;
  margin: 2rem auto 4rem;
  padding: 0 2rem;
}

/* Empty */
.empty-cart {
  text-align: center;
  padding: 5rem 1rem;
}
.empty-cart h2 { color: #333; margin: 1rem 0 0.3rem; font-weight: 700; }
.empty-cart p { color: #888; font-size: 0.9rem; margin: 0 0 1.5rem; }
.btn-continue {
  display: inline-block;
  padding: 0.7rem 2rem;
  background: #111;
  color: #fff;
  font-weight: 600;
  font-size: 0.9rem;
  text-decoration: none;
  transition: background 0.2s;
}
.btn-continue:hover { background: #333; }

/* Grid */
.cart-grid {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 2rem;
  align-items: flex-start;
}

/* Items */
.cart-items {
  background: #fff;
  border: 1px solid #e8e8e8;
}
.items-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 50px;
  padding: 0.8rem 1.2rem;
  background: #fafafa;
  border-bottom: 1px solid #e8e8e8;
  font-size: 0.78rem;
  font-weight: 700;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.cart-item {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 50px;
  padding: 1.2rem;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
}
.cart-item:last-child { border-bottom: none; }

.col-product {
  display: flex;
  gap: 1rem;
  align-items: center;
}
.item-image {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  background: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.item-image img { width: 100%; height: 100%; object-fit: cover; }
.img-placeholder { display: flex; align-items: center; justify-content: center; }
.item-info h4 {
  font-size: 0.9rem;
  font-weight: 600;
  color: #111;
  margin: 0 0 0.3rem;
  line-height: 1.3;
}
.item-variant {
  display: flex;
  gap: 0.8rem;
  font-size: 0.78rem;
  color: #888;
}

.col-price, .col-total {
  font-size: 0.9rem;
  color: #333;
}
.fw700 { font-weight: 700; color: #e94560; }

/* Qty */
.qty-control {
  display: inline-flex;
  align-items: center;
  border: 1.5px solid #ddd;
}
.qty-control button {
  width: 32px;
  height: 32px;
  border: none;
  background: #fafafa;
  font-size: 1rem;
  cursor: pointer;
  color: #333;
  transition: background 0.15s;
}
.qty-control button:hover { background: #eee; }
.qty-control button:disabled { opacity: 0.3; cursor: not-allowed; }
.qty-control span {
  width: 40px;
  text-align: center;
  font-size: 0.9rem;
  font-weight: 600;
  border-left: 1.5px solid #ddd;
  border-right: 1.5px solid #ddd;
  line-height: 32px;
}

.btn-remove {
  background: none;
  border: none;
  cursor: pointer;
  color: #ccc;
  padding: 0.3rem;
  transition: color 0.2s;
}
.btn-remove:hover { color: #e94560; }

/* Summary */
.summary-card {
  background: #fff;
  border: 1px solid #e8e8e8;
  padding: 1.5rem;
  position: sticky;
  top: 90px;
}
.summary-card h3 {
  font-size: 1rem;
  font-weight: 700;
  margin: 0 0 1.2rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid #111;
}
.summary-row {
  display: flex;
  justify-content: space-between;
  font-size: 0.88rem;
  color: #555;
  padding: 0.4rem 0;
}
.summary-row.total {
  font-weight: 700;
  color: #111;
  font-size: 1rem;
}
.total-price { color: #e94560; font-size: 1.15rem; }
.free-ship { color: #22c55e; font-weight: 600; }
.summary-divider {
  height: 1px;
  background: #e8e8e8;
  margin: 0.8rem 0;
}
.btn-checkout {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  width: 100%;
  padding: 0.8rem;
  background: #e94560;
  color: #fff;
  font-weight: 700;
  font-size: 0.95rem;
  text-decoration: none;
  margin-top: 1.2rem;
  transition: background 0.2s;
}
.btn-checkout:hover { background: #d63851; }
.btn-back-shop {
  display: block;
  text-align: center;
  margin-top: 0.8rem;
  font-size: 0.85rem;
  color: #888;
  text-decoration: none;
  transition: color 0.2s;
}
.btn-back-shop:hover { color: #111; }

/* Responsive */
@media (max-width: 900px) {
  .cart-grid { grid-template-columns: 1fr; }
  .items-header { display: none; }
  .cart-item {
    grid-template-columns: 1fr;
    gap: 0.8rem;
  }
  .col-price::before { content: 'Đơn giá: '; font-weight: 600; }
  .col-total::before { content: 'Thành tiền: '; font-weight: 600; }
}
</style>
