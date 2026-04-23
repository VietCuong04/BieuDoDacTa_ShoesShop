<template>
  <div class="checkout-page">
    <!-- Banner -->
    <div class="checkout-banner">
      <div class="banner-inner">
        <h1>Thanh toán</h1>
        <p>Hoàn tất đơn hàng của bạn</p>
      </div>
    </div>

    <!-- Not logged in -->
    <div v-if="!user" class="not-logged">
      <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
      <h2>Vui lòng đăng nhập</h2>
      <p>Bạn cần đăng nhập để đặt hàng</p>
      <router-link to="/login" class="btn-primary-act">Đăng nhập</router-link>
    </div>

    <!-- Empty cart -->
    <div v-else-if="cartItems.length === 0 && !orderSuccess" class="not-logged">
      <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.2">
        <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
        <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
      </svg>
      <h2>Giỏ hàng trống</h2>
      <p>Hãy thêm sản phẩm trước khi thanh toán</p>
      <router-link to="/products" class="btn-primary-act">Mua sắm ngay</router-link>
    </div>

    <!-- Order Success -->
    <div v-else-if="orderSuccess" class="success-screen">
      <div class="success-card">
        <div class="success-icon">
          <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="#22c55e" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
        </div>
        <h2>Đặt hàng thành công!</h2>
        <p>Mã đơn hàng của bạn: <strong>#{{ createdOrderId }}</strong></p>
        <p class="sub">Chúng tôi sẽ xử lý đơn hàng trong thời gian sớm nhất</p>
        <div class="success-actions">
          <router-link to="/profile" class="btn-primary-act">Xem đơn hàng</router-link>
          <router-link to="/products" class="btn-secondary-act">Tiếp tục mua sắm</router-link>
        </div>
      </div>
    </div>

    <!-- Checkout Form -->
    <div v-else class="checkout-container">
      <div class="checkout-grid">
        <!-- Form -->
        <div class="checkout-form">
          <div class="form-section">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              Thông tin giao hàng
            </h3>
            <div class="form-row">
              <div class="form-group">
                <label>Họ tên người nhận *</label>
                <input type="text" v-model="form.tenKhachHang" required placeholder="Nguyễn Văn A" />
              </div>
              <div class="form-group">
                <label>Số điện thoại *</label>
                <input type="tel" v-model="form.soDienThoai" required placeholder="0912345678" />
              </div>
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" v-model="form.email" placeholder="email@example.com" />
            </div>
            <div class="form-row form-row-3">
              <div class="form-group">
                <label>Tỉnh / Thành phố *</label>
                <select v-model="selectedProvince" @change="onProvinceChange" required>
                  <option value="">-- Chọn Tỉnh/TP --</option>
                  <option v-for="p in provinces" :key="p.code" :value="p.code">{{ p.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>Quận / Huyện *</label>
                <select v-model="selectedDistrict" @change="onDistrictChange" required :disabled="!selectedProvince">
                  <option value="">-- Chọn Quận/Huyện --</option>
                  <option v-for="d in districts" :key="d.code" :value="d.code">{{ d.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>Phường / Xã *</label>
                <select v-model="selectedWard" @change="onWardChange" required :disabled="!selectedDistrict">
                  <option value="">-- Chọn Phường/Xã --</option>
                  <option v-for="w in wards" :key="w.code" :value="w.code">{{ w.name }}</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label>Địa chỉ cụ thể * <span class="label-hint">(Số nhà, tên đường)</span></label>
              <input type="text" v-model="form.diaChiCuThe" required placeholder="Ví dụ: 123 Đường Lê Lợi" />
            </div>
          </div>

          <div class="form-section">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
              Phương thức thanh toán
            </h3>
            <div class="payment-options">
              <label class="payment-option" :class="{ active: form.pttt === 'COD' }">
                <input type="radio" v-model="form.pttt" value="COD" />
                <div class="option-content">
                  <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="2" y="6" width="20" height="12" rx="2"/><path d="M12 12a2 2 0 1 0 0-0.01"/><path d="M6 12h.01"/><path d="M18 12h.01"/></svg>
                  <div>
                    <strong>Thanh toán khi nhận hàng (COD)</strong>
                    <span>Thanh toán bằng tiền mặt khi nhận hàng</span>
                  </div>
                </div>
              </label>
              <label class="payment-option" :class="{ active: form.pttt === 'BANKING' }">
                <input type="radio" v-model="form.pttt" value="BANKING" />
                <div class="option-content">
                  <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
                  <div>
                    <strong>Chuyển khoản ngân hàng</strong>
                    <span>Thanh toán qua chuyển khoản</span>
                  </div>
                </div>
              </label>
            </div>
          </div>

          <div class="form-section banking-section" v-if="form.pttt === 'BANKING'">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
              Thông tin chuyển khoản
            </h3>
            <div class="banking-body">
              <!-- Left: bank details -->
              <div class="banking-details">
                <div class="bank-badge">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
                  <span>MBBank</span>
                </div>
                <div class="bank-rows">
                  <div class="bank-row">
                    <span class="bk-label">Số tài khoản</span>
                    <div class="bk-value-wrap">
                      <span class="bk-value mono">6838615102004</span>
                      <button class="btn-copy" @click="copyText('1025277267')" title="Sao chép">
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>
                      </button>
                    </div>
                  </div>
                  <div class="bank-row">
                    <span class="bk-label">Chủ tài khoản</span>
                    <span class="bk-value">SHOES SHOP</span>
                  </div>
                  <div class="bank-row">
                    <span class="bk-label">Nội dung CK</span>
                    <div class="bk-value-wrap">
                      <span class="bk-value mono">ShoesShop {{ user?.tenNguoiDung }} chuyển khoản </span>  
                      <button class="btn-copy" @click="copyText('ShoesShop ' + (user?.tenNguoiDung || ''))" title="Sao chép">
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="9" y="9" width="13" height="13" rx="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>
                      </button>
                    </div>
                  </div>
                  <div class="bank-row">
                    <span class="bk-label">Số tiền</span>
                    <span class="bk-value bk-amount">{{ formatPrice(subtotal) }}</span>
                  </div>
                </div>
                <div class="banking-note">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                  Đơn hàng sẽ được xử lý sau khi xác nhận thanh toán
                </div>
              </div>
              <!-- Right: QR -->
              <div class="banking-qr-wrap">
                <p class="qr-label">Quét mã QR để thanh toán</p>
                <div class="qr-frame">
                  <img src="/qr.jpg" alt="QR Code chuyển khoản" />
                </div>
                <p class="qr-sub">Hỗ trợ MoMo, ZaloPay, Banking App</p>
              </div>
            </div>
            <div v-if="copyToast" class="copy-toast">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
              Đã sao chép!
            </div>
          </div>

          <div class="form-section">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
              Ghi chú
            </h3>
            <textarea v-model="form.ghiChu" rows="3" placeholder="Ghi chú cho đơn hàng (tùy chọn)"></textarea>
          </div>
        </div>

        <!-- Order Summary -->
        <div class="order-summary">
          <div class="summary-card">
            <h3>Đơn hàng ({{ totalQty }} sản phẩm)</h3>
            <div class="summary-items">
              <div class="summary-item" v-for="(item, idx) in cartItems" :key="idx">
                <div class="si-img">
                  <img v-if="item.hinhAnh" :src="item.hinhAnh" alt="" />
                  <div v-else class="si-placeholder"></div>
                  <span class="si-qty">{{ item.soLuong }}</span>
                </div>
                <div class="si-info">
                  <span class="si-name">{{ item.tenSanPham }}</span>
                  <span class="si-variant">{{ item.mauSac }} / {{ item.size }}</span>
                </div>
                <span class="si-price">{{ formatPrice(item.giaTien * item.soLuong) }}</span>
              </div>
            </div>
            <div class="summary-divider"></div>
            <div class="summary-row">
              <span>Tạm tính</span>
              <span>{{ formatPrice(subtotal) }}</span>
            </div>
            <div class="summary-row">
              <span>Phí vận chuyển</span>
              <span class="free-ship">Miễn phí</span>
            </div>

            <!-- Coupon Input -->
            <div class="coupon-row">
              <div class="coupon-input-wrap" :class="{ applied: discount !== null }">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>
                <input
                  type="text"
                  v-model="couponCode"
                  placeholder="Nhập mã giảm giá"
                  :disabled="discount !== null"
                  @keyup.enter="applyCoupon"
                  class="coupon-input"
                />
                <button v-if="discount === null" class="btn-apply-coupon" @click="applyCoupon" :disabled="applyingCoupon || !couponCode.trim()">
                  <span v-if="applyingCoupon" class="spinner-xs"></span>
                  <span v-else>Áp dụng</span>
                </button>
                <button v-else class="btn-remove-coupon" @click="removeCoupon" title="Xóa mã">
                  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                </button>
              </div>
              <div v-if="couponError" class="coupon-error">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                {{ couponError }}
              </div>
              <div v-if="discount !== null" class="coupon-success">
                <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                {{ discount.tenGiamGia }} — Giảm {{ formatPrice(discount.soTienGiam) }}
              </div>
            </div>

            <div v-if="discount !== null" class="summary-row discount-row">
              <span>Giảm giá <span class="discount-code-badge">{{ couponCode }}</span></span>
              <span class="discount-amount">-{{ formatPrice(discount.soTienGiam) }}</span>
            </div>

            <div class="summary-divider"></div>
            <div class="summary-row total">
              <span>Tổng cộng</span>
              <span class="total-price">{{ formatPrice(finalTotal) }}</span>
            </div>
            <button class="btn-place-order" @click="placeOrder" :disabled="placing">
              <template v-if="placing">
                <span class="spinner-sm"></span> Đang xử lý...
              </template>
              <template v-else>Đặt hàng</template>
            </button>
            <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const API = 'http://localhost:8080/api'
const router = useRouter()

const user = ref(null)
const cartItems = ref([])
const placing = ref(false)
const errorMsg = ref('')
const orderSuccess = ref(false)
const createdOrderId = ref(null)
const copyToast = ref(false)

// Coupon
const couponCode = ref('')
const couponError = ref('')
const applyingCoupon = ref(false)
const discount = ref(null) // { maGiamGia, maCode, tenGiamGia, soTienGiam, soTienThanhToan }

async function applyCoupon() {
  couponError.value = ''
  if (!couponCode.value.trim()) return
  applyingCoupon.value = true
  try {
    const res = await fetch(`${API}/discounts/apply`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ maCode: couponCode.value.trim().toUpperCase(), orderTotal: subtotal.value }),
    })
    const data = await res.json()
    if (!res.ok) throw new Error(data.error || 'Mã không hợp lệ')
    discount.value = data
    couponCode.value = couponCode.value.trim().toUpperCase()
  } catch (e) {
    couponError.value = e.message
    discount.value = null
  } finally {
    applyingCoupon.value = false
  }
}

function removeCoupon() {
  discount.value = null
  couponError.value = ''
  couponCode.value = ''
}

const finalTotal = computed(() =>
  discount.value ? subtotal.value - discount.value.soTienGiam : subtotal.value
)

function copyText(text) {
  navigator.clipboard.writeText(text).then(() => {
    copyToast.value = true
    setTimeout(() => { copyToast.value = false }, 2000)
  })
}

const form = ref({
  tenKhachHang: '',
  soDienThoai: '',
  email: '',
  tinhThanhPho: '',
  quanHuyen: '',
  phuongXa: '',
  diaChiCuThe: '',
  diaChi: '',
  pttt: 'COD',
  ghiChu: '',
})

// Vietnam provinces API
const PROVINCES_API = 'https://provinces.open-api.vn/api'
const provinces = ref([])
const districts = ref([])
const wards = ref([])
const selectedProvince = ref('')
const selectedDistrict = ref('')
const selectedWard = ref('')

async function fetchProvinces() {
  try {
    const res = await fetch(`${PROVINCES_API}/p/`)
    if (res.ok) provinces.value = await res.json()
  } catch (e) { console.error('Failed to load provinces', e) }
}

async function onProvinceChange() {
  districts.value = []
  wards.value = []
  selectedDistrict.value = ''
  selectedWard.value = ''
  form.value.quanHuyen = ''
  form.value.phuongXa = ''
  if (!selectedProvince.value) { form.value.tinhThanhPho = ''; return }
  const prov = provinces.value.find(p => p.code === selectedProvince.value)
  form.value.tinhThanhPho = prov ? prov.name : ''
  try {
    const res = await fetch(`${PROVINCES_API}/p/${selectedProvince.value}?depth=2`)
    if (res.ok) {
      const data = await res.json()
      districts.value = data.districts || []
    }
  } catch (e) { console.error(e) }
}

async function onDistrictChange() {
  wards.value = []
  selectedWard.value = ''
  form.value.phuongXa = ''
  if (!selectedDistrict.value) { form.value.quanHuyen = ''; return }
  const dist = districts.value.find(d => d.code === selectedDistrict.value)
  form.value.quanHuyen = dist ? dist.name : ''
  try {
    const res = await fetch(`${PROVINCES_API}/d/${selectedDistrict.value}?depth=2`)
    if (res.ok) {
      const data = await res.json()
      wards.value = data.wards || []
    }
  } catch (e) { console.error(e) }
}

function onWardChange() {
  const w = wards.value.find(w => w.code === selectedWard.value)
  form.value.phuongXa = w ? w.name : ''
}

function loadCart() {
  const saved = localStorage.getItem('cart')
  cartItems.value = saved ? JSON.parse(saved) : []
}

function loadUser() {
  const saved = localStorage.getItem('user')
  user.value = saved ? JSON.parse(saved) : null
  if (user.value) {
    form.value.tenKhachHang = user.value.tenNguoiDung || ''
    form.value.soDienThoai = user.value.soDienThoai || ''
    form.value.email = user.value.email || user.value.tenDangNhap || ''
  }
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

const subtotal = computed(() => cartItems.value.reduce((s, i) => s + (i.giaTien || 0) * (i.soLuong || 0), 0))
const totalQty = computed(() => cartItems.value.reduce((s, i) => s + (i.soLuong || 0), 0))

async function placeOrder() {
  errorMsg.value = ''
  if (!form.value.tenKhachHang || !form.value.soDienThoai) {
    errorMsg.value = 'Vui lòng điền họ tên và số điện thoại'
    return
  }
  if (!form.value.tinhThanhPho || !form.value.quanHuyen || !form.value.phuongXa || !form.value.diaChiCuThe) {
    errorMsg.value = 'Vui lòng chọn đầy đủ địa chỉ giao hàng (Tỉnh/TP, Quận/Huyện, Phường/Xã, Địa chỉ cụ thể)'
    return
  }
  if (!form.value.pttt) {
    errorMsg.value = 'Vui lòng chọn phương thức thanh toán'
    return
  }

  // Build full address string
  const fullAddress = `${form.value.diaChiCuThe}, ${form.value.phuongXa}, ${form.value.quanHuyen}, ${form.value.tinhThanhPho}`

  placing.value = true
  try {
    // Create DonHang
    const orderBody = {
      tenKhachHang: form.value.tenKhachHang,
      soDienThoai: form.value.soDienThoai,
      email: form.value.email,
      tinhThanhPho: form.value.tinhThanhPho,
      quanHuyen: form.value.quanHuyen,
      phuongXa: form.value.phuongXa,
      diaChiCuThe: form.value.diaChiCuThe,
      diaChi: fullAddress,
      ghiChu: form.value.ghiChu,
      pttt: form.value.pttt,
      soTienBanDau: subtotal.value,
      soTienDuocGiam: discount.value ? discount.value.soTienGiam : 0,
      soTienThanhToan: finalTotal.value,
      maGiamGia: discount.value ? discount.value.maCode : null,
      maKhachHang: user.value.maTaiKhoan,
      trangThai: 'CHO_XAC_NHAN',
      nguoiKhoiTao: user.value.email || user.value.tenDangNhap,
    }

    const orderRes = await fetch(`${API}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(orderBody),
    })

    if (!orderRes.ok) {
      const err = await orderRes.json()
      throw new Error(err.error || 'Lỗi tạo đơn hàng')
    }

    const order = await orderRes.json()
    createdOrderId.value = order.maDonHang

    // Clear cart
    localStorage.removeItem('cart')
    cartItems.value = []
    window.dispatchEvent(new Event('cart-changed'))
    orderSuccess.value = true

  } catch (e) {
    errorMsg.value = e.message || 'Có lỗi xảy ra, vui lòng thử lại'
  } finally {
    placing.value = false
  }
}

onMounted(() => {
  loadUser()
  loadCart()
  fetchProvinces()
})
</script>

<style scoped>
.checkout-page { min-height: 80vh; }

.checkout-banner {
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

.checkout-container {
  max-width: 1320px;
  margin: 2rem auto 4rem;
  padding: 0 2rem;
}

/* Grid */
.checkout-grid {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 2rem;
  align-items: flex-start;
}

/* Form Sections */
.form-section {
  background: #fff;
  border: 1px solid #e8e8e8;
  padding: 1.5rem;
  margin-bottom: 1rem;
}
.form-section h3 {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 700;
  color: #111;
  margin: 0 0 1.2rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid #111;
}
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}
.form-row-3 {
  grid-template-columns: 1fr 1fr 1fr;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  margin-bottom: 1rem;
}
.form-group label {
  font-size: 0.82rem;
  font-weight: 600;
  color: #555;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.label-hint {
  text-transform: none;
  font-weight: 400;
  color: #999;
  letter-spacing: 0;
}
.form-group input,
.form-group select,
.checkout-page textarea {
  padding: 0.65rem 0.9rem;
  border: 1.5px solid #ddd;
  font-size: 0.9rem;
  color: #333;
  outline: none;
  transition: border-color 0.2s;
  font-family: inherit;
  width: 100%;
  background: #fff;
  border-radius: 0;
  appearance: auto;
}
.form-group input:focus,
.form-group select:focus,
.checkout-page textarea:focus {
  border-color: #111;
}
.form-group select:disabled {
  background: #f5f5f5;
  color: #aaa;
  cursor: not-allowed;
}
.checkout-page textarea {
  resize: vertical;
  min-height: 60px;
}

/* Payment */
.payment-options {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}
.payment-option {
  display: block;
  border: 1.5px solid #e8e8e8;
  padding: 1rem;
  cursor: pointer;
  transition: border-color 0.2s;
}
.payment-option:hover { border-color: #999; }
.payment-option.active { border-color: #111; background: #fafafa; }
.payment-option input[type="radio"] { display: none; }
.option-content {
  display: flex;
  gap: 0.8rem;
  align-items: flex-start;
}
.option-content svg { flex-shrink: 0; margin-top: 2px; }
.option-content strong {
  display: block;
  font-size: 0.9rem;
  color: #111;
  margin-bottom: 0.15rem;
}
.option-content span {
  font-size: 0.8rem;
  color: #888;
}

.banking-section { padding-bottom: 1.2rem; }
.banking-body {
  display: flex;
  gap: 2rem;
  align-items: flex-start;
}
.banking-details { flex: 1; min-width: 0; }
.bank-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: #006400;
  color: #fff;
  padding: 0.45rem 1rem;
  border-radius: 6px;
  font-size: 0.88rem;
  font-weight: 700;
  margin-bottom: 1.2rem;
  letter-spacing: 0.3px;
}
.bank-rows {
  display: flex;
  flex-direction: column;
  gap: 0;
  border: 1.5px solid #e8e8e8;
  border-radius: 0;
  overflow: hidden;
}
.bank-row {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #f0f0f0;
}
.bank-row:last-child { border-bottom: none; }
.bank-row:nth-child(odd) { background: #fafafa; }
.bk-label {
  font-size: 0.78rem;
  font-weight: 700;
  color: #888;
  text-transform: uppercase;
  letter-spacing: 0.4px;
  width: 130px;
  flex-shrink: 0;
}
.bk-value-wrap {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.bk-value {
  font-size: 0.92rem;
  font-weight: 700;
  color: #111;
}
.bk-value.mono {
  font-family: 'Courier New', monospace;
  letter-spacing: 0.5px;
  color: #1a1a2e;
}
.bk-value.bk-amount {
  color: #e94560;
  font-size: 1rem;
}
.btn-copy {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border: 1.5px solid #ddd;
  border-radius: 5px;
  background: #fff;
  cursor: pointer;
  color: #777;
  transition: all 0.15s;
  flex-shrink: 0;
  padding: 0;
}
.btn-copy:hover { border-color: #111; color: #111; background: #f5f5f5; }
.banking-note {
  display: flex;
  align-items: flex-start;
  gap: 0.4rem;
  margin-top: 0.9rem;
  font-size: 0.78rem;
  color: #888;
  padding: 0.6rem 0.8rem;
  background: #fffbeb;
  border: 1px solid #fde68a;
  border-radius: 6px;
  line-height: 1.5;
}
.banking-note svg { flex-shrink: 0; margin-top: 1px; color: #d97706; }
.banking-qr-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  width: 160px;
}
.qr-label {
  font-size: 0.75rem;
  font-weight: 700;
  color: #555;
  text-align: center;
  margin: 0 0 0.6rem;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}
.qr-frame {
  width: 148px;
  height: 148px;
  border: 3px solid #111;
  padding: 6px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}
.qr-frame img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}
.qr-sub {
  font-size: 0.7rem;
  color: #aaa;
  text-align: center;
  margin: 0.5rem 0 0;
  line-height: 1.4;
}
.copy-toast {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  margin-top: 0.8rem;
  padding: 0.5rem 0.9rem;
  background: #f0fdf4;
  border: 1px solid #bbf7d0;
  color: #16a34a;
  font-size: 0.82rem;
  font-weight: 600;
  border-radius: 6px;
}

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
  margin: 0 0 1rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid #111;
}
.summary-items {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
  max-height: 300px;
  overflow-y: auto;
}
.summary-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}
.si-img {
  position: relative;
  width: 50px;
  height: 50px;
  flex-shrink: 0;
  background: #f5f5f5;
  overflow: hidden;
}
.si-img img { width: 100%; height: 100%; object-fit: cover; }
.si-placeholder { width: 100%; height: 100%; background: #eee; }
.si-qty {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #111;
  color: #fff;
  font-size: 0.65rem;
  font-weight: 700;
  width: 18px;
  height: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.si-info {
  flex: 1;
  min-width: 0;
}
.si-name {
  display: block;
  font-size: 0.85rem;
  font-weight: 600;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.si-variant {
  font-size: 0.75rem;
  color: #999;
}
.si-price {
  font-size: 0.85rem;
  font-weight: 600;
  color: #333;
  white-space: nowrap;
}

.summary-divider { height: 1px; background: #e8e8e8; margin: 1rem 0; }
.summary-row {
  display: flex;
  justify-content: space-between;
  font-size: 0.88rem;
  color: #555;
  padding: 0.3rem 0;
}
.summary-row.total { font-weight: 700; color: #111; font-size: 1rem; }
.total-price { color: #e94560; font-size: 1.15rem; }
.free-ship { color: #22c55e; font-weight: 600; }

.btn-place-order {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  width: 100%;
  padding: 0.85rem;
  background: #e94560;
  color: #fff;
  border: none;
  font-weight: 700;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1.2rem;
  transition: background 0.2s;
}
.btn-place-order:hover { background: #d63851; }
.btn-place-order:disabled { opacity: 0.6; cursor: not-allowed; }

.error-msg {
  margin-top: 0.8rem;
  padding: 0.6rem 0.8rem;
  background: #ffeef0;
  color: #d32f2f;
  font-size: 0.85rem;
  font-weight: 500;
  border-left: 3px solid #d32f2f;
}

/* Not logged / empty */
.not-logged {
  text-align: center;
  padding: 5rem 1rem;
  max-width: 400px;
  margin: 0 auto;
}
.not-logged h2 { color: #333; margin: 1rem 0 0.3rem; font-weight: 700; }
.not-logged p { color: #888; font-size: 0.9rem; margin: 0 0 1.5rem; }

.btn-primary-act {
  display: inline-block;
  padding: 0.7rem 2rem;
  background: #111;
  color: #fff;
  font-weight: 600;
  font-size: 0.9rem;
  text-decoration: none;
  transition: background 0.2s;
}
.btn-primary-act:hover { background: #333; }

/* Success */
.success-screen {
  display: flex;
  justify-content: center;
  padding: 4rem 2rem;
}
.success-card {
  text-align: center;
  background: #fff;
  border: 1px solid #e8e8e8;
  padding: 3rem 2.5rem;
  max-width: 480px;
}
.success-icon { margin-bottom: 1rem; }
.success-card h2 {
  font-size: 1.4rem;
  font-weight: 800;
  color: #22c55e;
  margin: 0 0 0.5rem;
}
.success-card p {
  font-size: 1rem;
  color: #333;
  margin: 0 0 0.3rem;
}
.success-card .sub {
  font-size: 0.85rem;
  color: #888;
  margin-bottom: 1.5rem;
}
.success-actions {
  display: flex;
  gap: 0.8rem;
  justify-content: center;
  flex-wrap: wrap;
}
.btn-secondary-act {
  display: inline-block;
  padding: 0.7rem 2rem;
  background: #fff;
  color: #111;
  border: 1.5px solid #111;
  font-weight: 600;
  font-size: 0.9rem;
  text-decoration: none;
  transition: all 0.2s;
}
.btn-secondary-act:hover { background: #111; color: #fff; }

.spinner-sm {
  width: 18px; height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  display: inline-block;
}
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 900px) {
  .checkout-grid { grid-template-columns: 1fr; }
  .form-row { grid-template-columns: 1fr; }
  .form-row-3 { grid-template-columns: 1fr; }
  .banking-body { flex-direction: column-reverse; align-items: center; }
  .banking-qr-wrap { width: 100%; flex-direction: row; gap: 1.2rem; align-items: center; justify-content: center; }
  .qr-label { margin: 0; }
}

/* Coupon */
.coupon-row { margin: 0.8rem 0 0; }
.coupon-input-wrap {
  display: flex;
  align-items: center;
  border: 1.5px solid #e8e8e8;
  background: #fafafa;
  padding: 0 0 0 0.7rem;
  gap: 0.4rem;
  transition: border-color 0.2s;
}
.coupon-input-wrap:focus-within { border-color: #111; background: #fff; }
.coupon-input-wrap.applied { border-color: #22c55e; background: #f0fdf4; }
.coupon-input-wrap svg { flex-shrink: 0; color: #aaa; }
.coupon-input-wrap.applied svg { color: #22c55e; }
.coupon-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: 0.65rem 0.3rem;
  font-size: 0.88rem;
  font-weight: 600;
  color: #333;
  outline: none;
  font-family: monospace;
  letter-spacing: 0.5px;
  text-transform: uppercase;
  width: 100%;
}
.coupon-input:disabled { color: #16a34a; cursor: default; }
.btn-apply-coupon {
  flex-shrink: 0;
  padding: 0 1rem;
  height: 40px;
  background: #111;
  color: #fff;
  border: none;
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
}
.btn-apply-coupon:hover:not(:disabled) { background: #333; }
.btn-apply-coupon:disabled { background: #ccc; cursor: not-allowed; }
.btn-remove-coupon {
  flex-shrink: 0;
  width: 36px;
  height: 40px;
  border: none;
  background: transparent;
  cursor: pointer;
  color: #888;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.15s;
}
.btn-remove-coupon:hover { color: #e94560; }
.coupon-error {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  margin-top: 0.4rem;
  font-size: 0.78rem;
  color: #dc2626;
  font-weight: 500;
}
.coupon-success {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  margin-top: 0.4rem;
  font-size: 0.78rem;
  color: #16a34a;
  font-weight: 600;
}
.discount-row { color: #16a34a !important; }
.discount-amount { color: #16a34a; font-weight: 700; }
.discount-code-badge {
  display: inline-block;
  background: #dcfce7;
  color: #15803d;
  font-size: 0.7rem;
  font-weight: 800;
  padding: 0.1rem 0.45rem;
  border-radius: 4px;
  font-family: monospace;
  letter-spacing: 0.5px;
  margin-left: 0.3rem;
}
.spinner-xs {
  width: 13px; height: 13px;
  border: 2px solid rgba(255,255,255,0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  display: inline-block;
}
</style>
