<template>
  <div class="profile-page">
    <!-- Banner -->
    <div class="profile-banner">
      <div class="banner-inner">
        <h1>Tài khoản của tôi</h1>
        <p>Quản lý thông tin cá nhân và theo dõi đơn hàng</p>
      </div>
    </div>

    <div class="profile-container" v-if="user">
      <div class="profile-grid">
        <!-- Sidebar -->
        <div class="profile-sidebar">
          <div class="sidebar-card">
            <div class="user-avatar-lg">{{ user.tenNguoiDung?.charAt(0)?.toUpperCase() || '?' }}</div>
            <h3>{{ user.tenNguoiDung }}</h3>
            <p class="user-email">{{ user.email }}</p>
            <div class="sidebar-nav">
              <button :class="{ active: activeTab === 'info' }" @click="activeTab = 'info'">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                Thông tin cá nhân
              </button>
              <button :class="{ active: activeTab === 'orders' }" @click="activeTab = 'orders'">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
                Đơn hàng của tôi
              </button>
              <button :class="{ active: activeTab === 'password' }" @click="activeTab = 'password'">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                Đổi mật khẩu
              </button>
            </div>
          </div>
        </div>

        <!-- Content -->
        <div class="profile-content">
          <!-- Info Tab -->
          <div v-if="activeTab === 'info'" class="content-card">
            <h2>Thông tin cá nhân</h2>
            <div v-if="infoMsg" class="msg" :class="infoMsgType">{{ infoMsg }}</div>
            <form @submit.prevent="saveProfile">
              <div class="form-row">
                <div class="form-group">
                  <label>Họ tên</label>
                  <input type="text" v-model="form.tenNguoiDung" required />
                </div>
                <div class="form-group">
                  <label>Email</label>
                  <input type="email" :value="user.email" disabled class="disabled" />
                  <span class="hint">Email không thể thay đổi</span>
                </div>
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label>Số điện thoại</label>
                  <input type="text" v-model="form.soDienThoai" placeholder="0xxxxxxxxx" />
                </div>
                <div class="form-group">
                  <label>Giới tính</label>
                  <select v-model="form.gioiTinh">
                    <option value="">-- Chọn --</option>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                    <option value="Khác">Khác</option>
                  </select>
                </div>
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label>Năm sinh</label>
                  <input type="date" v-model="formNamSinh" />
                </div>
                <div class="form-group">
                  <label>Ngày tham gia</label>
                  <input type="text" :value="formatDate(user.ngayKhoiTao)" disabled class="disabled" />
                </div>
              </div>
              <button type="submit" class="btn-save" :disabled="saving">
                <template v-if="saving">
                  <span class="spinner-sm"></span> Đang lưu...
                </template>
                <template v-else>Lưu thay đổi</template>
              </button>
            </form>
          </div>

          <!-- Orders Tab -->
          <div v-if="activeTab === 'orders'" class="content-card">
            <h2>Đơn hàng của tôi</h2>
            <div v-if="loadingOrders" class="loading-box">
              <div class="spinner-sm"></div> Đang tải...
            </div>
            <div v-else-if="orders.length === 0" class="empty-state">
              <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
              <p>Bạn chưa có đơn hàng nào</p>
              <router-link to="/products" class="btn-shop">Mua sắm ngay</router-link>
            </div>
            <div v-else class="orders-list">
              <div class="order-card" v-for="o in orders" :key="o.maDonHang">
                <div class="order-header">
                  <span class="order-id">#{{ o.maDonHang }}</span>
                  <span :class="'status-tag ' + statusKey(o.trangThai)">{{ statusLabel(o.trangThai) }}</span>
                </div>
                <div class="order-body">
                  <div class="order-detail-row">
                    <span>Ngày đặt:</span>
                    <span>{{ formatDate(o.ngayKhoiTao) }}</span>
                  </div>
                  <div class="order-detail-row">
                    <span>Địa chỉ:</span>
                    <span>{{ buildAddress(o) }}</span>
                  </div>
                  <div class="order-detail-row">
                    <span>Thanh toán:</span>
                    <span>{{ o.pttt || '—' }}</span>
                  </div>
                  <div class="order-detail-row total">
                    <span>Tổng tiền:</span>
                    <span class="order-total">{{ formatPrice(o.soTienThanhToan) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Password Tab -->
          <div v-if="activeTab === 'password'" class="content-card">
            <h2>Đổi mật khẩu</h2>
            <div v-if="pwMsg" class="msg" :class="pwMsgType">{{ pwMsg }}</div>
            <form @submit.prevent="changePassword">
              <div class="form-group">
                <label>Mật khẩu hiện tại</label>
                <input type="password" v-model="pwForm.oldPassword" required />
              </div>
              <div class="form-group">
                <label>Mật khẩu mới</label>
                <input type="password" v-model="pwForm.newPassword" required minlength="6" />
              </div>
              <div class="form-group">
                <label>Xác nhận mật khẩu mới</label>
                <input type="password" v-model="pwForm.confirmPassword" required minlength="6" />
              </div>
              <button type="submit" class="btn-save" :disabled="savingPw">
                <template v-if="savingPw">
                  <span class="spinner-sm"></span> Đang xử lý...
                </template>
                <template v-else>Đổi mật khẩu</template>
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Not logged in -->
    <div v-else class="not-logged-in">
      <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
      <h2>Vui lòng đăng nhập</h2>
      <p>Bạn cần đăng nhập để xem trang cá nhân</p>
      <router-link to="/login" class="btn-save">Đăng nhập</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'

const API = 'http://localhost:8080/api'
const route = useRoute()

const user = ref(null)
const activeTab = ref('info')
const saving = ref(false)
const savingPw = ref(false)
const infoMsg = ref('')
const infoMsgType = ref('success')
const pwMsg = ref('')
const pwMsgType = ref('success')
const loadingOrders = ref(false)
const orders = ref([])

const form = ref({ tenNguoiDung: '', soDienThoai: '', gioiTinh: '' })
const formNamSinh = ref('')
const pwForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

function loadUser() {
  const saved = localStorage.getItem('user')
  user.value = saved ? JSON.parse(saved) : null
  if (user.value) {
    form.value = {
      tenNguoiDung: user.value.tenNguoiDung || '',
      soDienThoai: user.value.soDienThoai || '',
      gioiTinh: user.value.gioiTinh || '',
    }
    if (user.value.namSinh) {
      formNamSinh.value = user.value.namSinh.substring(0, 10)
    }
  }
}

function formatPrice(v) {
  if (v == null) return '—'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
}

function formatDate(d) {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

function buildAddress(o) {
  if (o.diaChiCuThe && o.phuongXa && o.quanHuyen && o.tinhThanhPho) {
    return `${o.diaChiCuThe}, ${o.phuongXa}, ${o.quanHuyen}, ${o.tinhThanhPho}`
  }
  return o.diaChi || '—'
}

function statusLabel(s) {
  const m = { CHO_XAC_NHAN: 'Chờ xác nhận', DA_XAC_NHAN: 'Đã xác nhận', DANG_GIAO: 'Đang giao', HOAN_THANH: 'Hoàn thành', DA_HUY: 'Đã hủy' }
  return m[s] || s || '—'
}
function statusKey(s) {
  const m = { CHO_XAC_NHAN: 'pending', DA_XAC_NHAN: 'confirmed', DANG_GIAO: 'shipping', HOAN_THANH: 'done', DA_HUY: 'cancelled' }
  return m[s] || 'pending'
}

async function saveProfile() {
  saving.value = true
  infoMsg.value = ''
  try {
    const body = { ...form.value }
    if (formNamSinh.value) {
      body.namSinh = formNamSinh.value + 'T00:00:00'
    }
    const res = await fetch(`${API}/user/${user.value.maTaiKhoan}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
    const data = await res.json()
    if (res.ok) {
      localStorage.setItem('user', JSON.stringify(data))
      user.value = data
      window.dispatchEvent(new Event('user-changed'))
      infoMsg.value = 'Cập nhật thành công!'
      infoMsgType.value = 'success'
    } else {
      infoMsg.value = data.error || 'Có lỗi xảy ra'
      infoMsgType.value = 'error'
    }
  } catch (e) {
    infoMsg.value = 'Lỗi kết nối server'
    infoMsgType.value = 'error'
  } finally {
    saving.value = false
  }
}

async function changePassword() {
  pwMsg.value = ''
  if (pwForm.value.newPassword !== pwForm.value.confirmPassword) {
    pwMsg.value = 'Mật khẩu xác nhận không khớp'
    pwMsgType.value = 'error'
    return
  }
  savingPw.value = true
  try {
    const res = await fetch(`${API}/user/${user.value.maTaiKhoan}/password`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ oldPassword: pwForm.value.oldPassword, newPassword: pwForm.value.newPassword }),
    })
    const data = await res.json()
    if (res.ok) {
      pwMsg.value = 'Đổi mật khẩu thành công!'
      pwMsgType.value = 'success'
      pwForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
    } else {
      pwMsg.value = data.error || 'Có lỗi xảy ra'
      pwMsgType.value = 'error'
    }
  } catch (e) {
    pwMsg.value = 'Lỗi kết nối server'
    pwMsgType.value = 'error'
  } finally {
    savingPw.value = false
  }
}

async function loadOrders() {
  if (!user.value) return
  loadingOrders.value = true
  try {
    const res = await fetch(`${API}/orders/customer/${user.value.maTaiKhoan}`)
    if (res.ok) {
      orders.value = await res.json()
      orders.value.sort((a, b) => new Date(b.ngayKhoiTao) - new Date(a.ngayKhoiTao))
    }
  } catch (e) { console.error(e) }
  finally { loadingOrders.value = false }
}

watch(activeTab, (tab) => {
  if (tab === 'orders' && orders.value.length === 0) {
    loadOrders()
  }
})

onMounted(() => {
  loadUser()
  window.addEventListener('user-changed', loadUser)
})
</script>

<style scoped>
.profile-page { min-height: 80vh; }

.profile-banner {
  background: #111;
  color: #fff;
  padding: 3rem 2rem 2.5rem;
}
.banner-inner {
  max-width: 1320px;
  margin: 0 auto;
}
.banner-inner h1 {
  font-size: 1.8rem;
  font-weight: 800;
  margin: 0 0 0.3rem;
}
.banner-inner p {
  font-size: 0.9rem;
  color: #aaa;
  margin: 0;
}

.profile-container {
  max-width: 1320px;
  margin: -1.5rem auto 3rem;
  padding: 0 2rem;
}
.profile-grid {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 1.5rem;
}

/* Sidebar */
.sidebar-card {
  background: #fff;
  border: 1px solid #e8e8e8;
  padding: 2rem 1.5rem;
  text-align: center;
}
.user-avatar-lg {
  width: 72px;
  height: 72px;
  margin: 0 auto 1rem;
  background: linear-gradient(135deg, #e94560, #c0392b);
  color: #fff;
  font-size: 1.6rem;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
}
.sidebar-card h3 {
  font-size: 1.1rem;
  font-weight: 700;
  color: #111;
  margin: 0 0 0.2rem;
}
.user-email {
  font-size: 0.82rem;
  color: #888;
  margin: 0 0 1.5rem;
  word-break: break-all;
}
.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}
.sidebar-nav button {
  display: flex;
  align-items: center;
  gap: 0.7rem;
  padding: 0.7rem 1rem;
  background: transparent;
  border: none;
  font-size: 0.88rem;
  font-weight: 500;
  color: #555;
  cursor: pointer;
  transition: all 0.15s;
  text-align: left;
}
.sidebar-nav button:hover {
  background: #f5f5f5;
  color: #111;
}
.sidebar-nav button.active {
  background: #111;
  color: #fff;
}
.sidebar-nav button.active svg { stroke: #fff; }

/* Content */
.content-card {
  background: #fff;
  border: 1px solid #e8e8e8;
  padding: 2rem;
}
.content-card h2 {
  font-size: 1.2rem;
  font-weight: 700;
  color: #111;
  margin: 0 0 1.5rem;
  padding-bottom: 0.8rem;
  border-bottom: 2px solid #111;
}

/* Form */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  margin-bottom: 0.5rem;
}
.form-group label {
  font-size: 0.82rem;
  font-weight: 600;
  color: #555;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.form-group input,
.form-group select {
  padding: 0.65rem 0.9rem;
  border: 1.5px solid #ddd;
  font-size: 0.9rem;
  color: #333;
  outline: none;
  transition: border-color 0.2s;
  background: #fff;
}
.form-group input:focus,
.form-group select:focus {
  border-color: #111;
}
.form-group input.disabled {
  background: #f5f5f5;
  color: #999;
  cursor: not-allowed;
}
.hint {
  font-size: 0.75rem;
  color: #aaa;
}
.btn-save {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.7rem 2rem;
  background: #111;
  color: #fff;
  border: none;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
  margin-top: 0.5rem;
  text-decoration: none;
}
.btn-save:hover { background: #333; }
.btn-save:disabled { opacity: 0.6; cursor: not-allowed; }

/* Messages */
.msg {
  padding: 0.7rem 1rem;
  margin-bottom: 1rem;
  font-size: 0.88rem;
  font-weight: 500;
}
.msg.success { background: #e8f5e9; color: #2e7d32; border-left: 3px solid #2e7d32; }
.msg.error { background: #ffeef0; color: #d32f2f; border-left: 3px solid #d32f2f; }

/* Orders */
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.order-card {
  border: 1px solid #e8e8e8;
  transition: border-color 0.2s;
}
.order-card:hover { border-color: #111; }
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 1.2rem;
  background: #fafafa;
  border-bottom: 1px solid #e8e8e8;
}
.order-id {
  font-weight: 700;
  font-size: 0.95rem;
  color: #111;
}
.status-tag {
  font-size: 0.75rem;
  font-weight: 700;
  padding: 0.25rem 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.status-tag.pending { background: #fff3cd; color: #856404; }
.status-tag.confirmed { background: #d1ecf1; color: #0c5460; }
.status-tag.shipping { background: #e8e0ff; color: #4a3aab; }
.status-tag.done { background: #d4edda; color: #155724; }
.status-tag.cancelled { background: #f8d7da; color: #721c24; }
.order-body { padding: 1rem 1.2rem; }
.order-detail-row {
  display: flex;
  justify-content: space-between;
  padding: 0.35rem 0;
  font-size: 0.88rem;
  color: #555;
}
.order-detail-row.total {
  margin-top: 0.5rem;
  padding-top: 0.6rem;
  border-top: 1px solid #e8e8e8;
  font-weight: 600;
  color: #111;
}
.order-total {
  color: #e94560;
  font-weight: 700;
  font-size: 1rem;
}

/* Empty / Not Logged In */
.empty-state, .not-logged-in {
  text-align: center;
  padding: 3rem 1rem;
  color: #888;
}
.empty-state p, .not-logged-in p {
  margin: 0.5rem 0;
  font-size: 0.9rem;
}
.not-logged-in {
  max-width: 400px;
  margin: 4rem auto;
}
.not-logged-in h2 {
  color: #333;
  margin: 1rem 0 0.3rem;
}
.btn-shop {
  display: inline-block;
  margin-top: 1rem;
  padding: 0.6rem 1.8rem;
  background: #111;
  color: #fff;
  font-weight: 600;
  font-size: 0.88rem;
  text-decoration: none;
  transition: background 0.2s;
}
.btn-shop:hover { background: #333; }

/* Loading */
.loading-box {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #888;
  padding: 2rem;
  justify-content: center;
}
.spinner-sm {
  width: 18px;
  height: 18px;
  border: 2px solid #ddd;
  border-top-color: #111;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
  display: inline-block;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* Responsive */
@media (max-width: 768px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
