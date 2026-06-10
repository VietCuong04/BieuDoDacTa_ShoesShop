<template>
  <div class="auth-page">
    <div class="auth-layout">
      <!-- Left side - branding -->
      <div class="auth-brand">
        <div class="brand-content">
          <div class="brand-logo">
            <span class="logo-circle">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><line x1="19" y1="8" x2="19" y2="14"/><line x1="22" y1="11" x2="16" y2="11"/></svg>
            </span>
          </div>
          <h1>ShoesShop</h1>
          <p>Đăng ký tài khoản để khám phá hàng ngàn mẫu giày thời trang mới nhất</p>
          <div class="brand-features">
            <div class="bf-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
              <span>Ưu đãi dành riêng thành viên</span>
            </div>
            <div class="bf-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
              <span>Theo dõi đơn hàng dễ dàng</span>
            </div>
            <div class="bf-item">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
              <span>Tích điểm với mỗi đơn hàng</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right side - form -->
      <div class="auth-form-side">
        <div class="auth-box">
          <h2>Đăng ký</h2>
          <p class="auth-subtitle">Tạo tài khoản mới để mua sắm</p>

          <form @submit.prevent="handleRegister">
            <div class="form-group">
              <label>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                Họ và tên
              </label>
              <input v-model="form.tenNguoiDung" type="text" placeholder="Nhập họ và tên" required />
            </div>
            <div class="form-group">
              <label>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                Email
              </label>
              <input v-model="form.email" type="email" placeholder="email@example.com" required />
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
                  Số điện thoại
                </label>
                <input v-model="form.soDienThoai" type="tel" placeholder="0901234567" />
              </div>
              <div class="form-group">
                <label>
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  Giới tính
                </label>
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
                <label>
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                  Năm sinh
                </label>
                <input v-model="form.namSinh" type="date" />
              </div>
            </div>
            <div class="form-group">
              <label>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                Mật khẩu
              </label>
              <input v-model="form.matKhau" type="password" placeholder="Nhập mật khẩu" required />
            </div>
            <div class="form-group">
              <label>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/><circle cx="12" cy="16" r="1"/></svg>
                Xác nhận mật khẩu
              </label>
              <input v-model="confirmPassword" type="password" placeholder="Nhập lại mật khẩu" required />
            </div>

            <p v-if="error" class="error-msg">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
              {{ error }}
            </p>
            <p v-if="success" class="success-msg">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
              {{ success }}
            </p>

            <button type="submit" class="btn-submit" :disabled="loading">
              <span v-if="loading" class="btn-spinner"></span>
              {{ loading ? 'Đang xử lý...' : 'Đăng ký ngay' }}
            </button>
          </form>

          <p class="switch-link">
            Bạn đã có tài khoản?
            <router-link to="/login">Đăng nhập ngay</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  tenNguoiDung: '',
  email: '',
  matKhau: '',
  soDienThoai: '',
  gioiTinh: '',
  namSinh: '',
})
const confirmPassword = ref('')
const error = ref('')
const success = ref('')
const loading = ref(false)

async function handleRegister() {
  error.value = ''
  success.value = ''

  if (form.value.matKhau !== confirmPassword.value) {
    error.value = 'Mật khẩu xác nhận không khớp'
    return
  }

  loading.value = true
  try {
    const body = { ...form.value }
    if (body.namSinh) {
      body.namSinh = body.namSinh + 'T00:00:00'
    }

    const res = await fetch('https://shoes-web-be-t7xh.onrender.com/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      const data = await res.json().catch(() => null)
      throw new Error(data?.error || 'Đăng ký thất bại')
    }

    success.value = 'Đăng ký thành công! Đang chuyển trang...'
    setTimeout(() => router.push('/login'), 1500)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - 140px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  padding: 2rem;
}
.auth-layout {
  display: flex;
  max-width: 960px;
  width: 100%;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 8px 48px rgba(0, 0, 0, 0.1);
}

/* Brand side */
.auth-brand {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  color: #fff;
  padding: 3rem 2.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 340px;
  position: relative;
  overflow: hidden;
}
.auth-brand::after {
  content: '';
  position: absolute;
  width: 250px;
  height: 250px;
  border-radius: 50%;
  background: rgba(233, 69, 96, 0.08);
  top: -80px;
  right: -80px;
}
.auth-brand::before {
  content: '';
  position: absolute;
  width: 180px;
  height: 180px;
  border-radius: 50%;
  background: rgba(79, 172, 254, 0.06);
  bottom: -60px;
  left: -60px;
}
.brand-content {
  text-align: center;
  position: relative;
  z-index: 1;
}
.brand-logo {
  margin-bottom: 1.2rem;
}
.logo-circle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  border-radius: 18px;
  background: rgba(233, 69, 96, 0.2);
  border: 1px solid rgba(233, 69, 96, 0.3);
}
.brand-content h1 {
  font-size: 1.8rem;
  font-weight: 800;
  margin-bottom: 0.7rem;
}
.brand-content p {
  color: rgba(255,255,255,0.7);
  line-height: 1.6;
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}
.brand-features {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}
.bf-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.82rem;
  color: rgba(255,255,255,0.6);
}
.bf-item svg {
  color: #4facfe;
  flex-shrink: 0;
}

/* Form side */
.auth-form-side {
  background: #fff;
  flex: 1;
  padding: 2.5rem;
  overflow-y: auto;
  max-height: 90vh;
}
.auth-box h2 {
  font-size: 1.7rem;
  color: #1a1a2e;
  margin-bottom: 0.3rem;
  font-weight: 800;
}
.auth-subtitle {
  color: #888;
  margin-bottom: 1.8rem;
  font-size: 0.9rem;
}

.form-group {
  margin-bottom: 1.1rem;
  flex: 1;
}
.form-group label {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.82rem;
  font-weight: 600;
  color: #444;
  margin-bottom: 0.4rem;
}
.form-group label svg {
  color: #888;
}
.form-group input,
.form-group select {
  width: 100%;
  padding: 0.7rem 1rem;
  border: 1.5px solid #e8e8e8;
  border-radius: 10px;
  font-size: 0.9rem;
  transition: all 0.2s;
  box-sizing: border-box;
  background: #fafafa;
}
.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #e94560;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.08);
}
.form-row {
  display: flex;
  gap: 1rem;
}

.btn-submit {
  width: 100%;
  padding: 0.8rem;
  background: #e94560;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  margin-top: 0.5rem;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}
.btn-submit:hover {
  background: #d63851;
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(233, 69, 96, 0.3);
}
.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}
.btn-spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}

.switch-link {
  text-align: center;
  margin-top: 1.3rem;
  color: #888;
  font-size: 0.9rem;
}
.switch-link a {
  color: #e94560;
  font-weight: 700;
  text-decoration: none;
}
.switch-link a:hover {
  text-decoration: underline;
}

.error-msg {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.4rem;
  color: #d32f2f;
  font-size: 0.85rem;
  text-align: center;
  margin-bottom: 0.8rem;
  padding: 0.6rem;
  background: #ffeef0;
  border-radius: 8px;
}
.success-msg {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.4rem;
  color: #2e7d32;
  font-size: 0.85rem;
  text-align: center;
  margin-bottom: 0.8rem;
  padding: 0.6rem;
  background: #e8f5e9;
  border-radius: 8px;
}

@media (max-width: 900px) {
  .auth-page { padding: 1.5rem; }
  .auth-brand { 
    flex: 0 0 280px;
    padding: 2.5rem 2rem;
  }
  .brand-content h1 { font-size: 1.6rem; }
  .brand-content p { font-size: 0.85rem; }
  .bf-item { font-size: 0.78rem; }
  .auth-form-side { padding: 2.5rem 2rem; }
  .auth-box h2 { font-size: 1.5rem; }
}

@media (max-width: 768px) {
  .auth-layout {
    flex-direction: column;
    border-radius: 16px;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  }
  .auth-brand {
    flex: none;
    padding: 2rem 1.5rem;
    min-height: 200px;
    justify-content: center;
  }
  .auth-brand::after {
    width: 200px;
    height: 200px;
    top: -60px;
    right: -60px;
  }
  .auth-brand::before {
    width: 150px;
    height: 150px;
    bottom: -50px;
    left: -50px;
  }
  .brand-content h1 { font-size: 1.4rem; margin-bottom: 0.5rem; }
  .brand-content p { font-size: 0.8rem; line-height: 1.5; margin-bottom: 1rem; }
  .brand-features { display: none; }
  .logo-circle { width: 56px; height: 56px; }
  
  .auth-form-side {
    padding: 2rem 1.5rem;
    max-height: none;
    overflow-y: auto;
  }
  .auth-box h2 { font-size: 1.3rem; }
  .auth-subtitle { margin-bottom: 1.5rem; font-size: 0.85rem; }
  
  .form-group { margin-bottom: 1rem; }
  .form-group label { font-size: 0.82rem; margin-bottom: 0.4rem; }
  .form-group input,
  .form-group select {
    padding: 0.65rem 0.95rem;
    font-size: 0.9rem;
    border-radius: 8px;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  .form-row .form-group {
    margin-bottom: 1rem;
  }
  
  .btn-submit { 
    padding: 0.75rem;
    font-size: 0.95rem;
    margin-top: 0.4rem;
  }
  
  .switch-link { margin-top: 1.2rem; font-size: 0.85rem; }
  .error-msg, .success-msg { font-size: 0.8rem; padding: 0.5rem; }
}

@media (max-width: 640px) {
  .auth-page { 
    padding: 1rem;
    min-height: calc(100vh - 80px);
  }
  
  .auth-layout {
    border-radius: 12px;
  }
  
  .auth-brand { 
    padding: 1.5rem;
    min-height: auto;
  }
  .brand-logo { margin-bottom: 0.8rem; }
  .logo-circle { width: 48px; height: 48px; }
  .brand-content h1 { font-size: 1.2rem; margin-bottom: 0.4rem; }
  .brand-content p { font-size: 0.75rem; margin-bottom: 0.8rem; }
  .auth-brand::after { display: none; }
  .auth-brand::before { display: none; }
  
  .auth-form-side { 
    padding: 1.5rem 1.2rem;
    max-height: none;
  }
  .auth-box h2 { font-size: 1.15rem; }
  .auth-subtitle { margin-bottom: 1.2rem; font-size: 0.8rem; }
  
  .form-group { margin-bottom: 0.9rem; }
  .form-group label { font-size: 0.8rem; }
  .form-group input,
  .form-group select {
    padding: 0.6rem 0.9rem;
    font-size: 0.85rem;
  }
  
  .form-row { gap: 0; }
  
  .btn-submit {
    padding: 0.65rem;
    font-size: 0.9rem;
  }
  .btn-spinner { width: 16px; height: 16px; border-width: 2px; }
  
  .switch-link { margin-top: 1rem; font-size: 0.8rem; }
  .error-msg, .success-msg { font-size: 0.75rem; }
}

@media (max-width: 480px) {
  .auth-page { 
    padding: 0.8rem;
    min-height: calc(100vh - 60px);
  }
  
  .auth-brand { 
    padding: 1.2rem;
    min-height: auto;
  }
  .brand-logo { margin-bottom: 0.6rem; }
  .logo-circle { width: 44px; height: 44px; }
  .brand-content h1 { font-size: 1.1rem; margin-bottom: 0.3rem; }
  .brand-content p { font-size: 0.7rem; margin-bottom: 0.6rem; }
  
  .auth-form-side { padding: 1.2rem 1rem; }
  .auth-box h2 { font-size: 1.05rem; }
  .auth-subtitle { margin-bottom: 1rem; font-size: 0.75rem; }
  
  .form-group { margin-bottom: 0.8rem; }
  .form-group label { font-size: 0.75rem; }
  .form-group input,
  .form-group select {
    padding: 0.55rem 0.8rem;
    font-size: 0.8rem;
    border-radius: 6px;
  }
  
  .btn-submit {
    padding: 0.6rem;
    font-size: 0.85rem;
  }
  
  .switch-link { 
    margin-top: 0.8rem;
    font-size: 0.75rem;
  }
}
</style>
