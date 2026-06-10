<template>
  <div class="login-page d-flex align-items-center justify-content-center min-vh-100">
    <div class="card border-0 shadow-lg" style="width:400px;max-width:90vw">
      <div class="card-body p-4">
        <div class="text-center mb-4">
          <div class="d-inline-flex align-items-center justify-content-center rounded-circle bg-primary bg-opacity-10 mb-3" style="width:56px;height:56px">
            <i class="bi bi-shop fs-3 text-primary"></i>
          </div>
          <h4 class="fw-bold mb-1">ShoesShop Admin</h4>
          <p class="text-muted small mb-0">Đăng nhập quản trị</p>
        </div>
        <form @submit.prevent="handleLogin">
          <div class="mb-3">
            <label class="form-label fw-semibold small">Email</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
              <input v-model="form.email" type="email" class="form-control border-start-0" placeholder="Nhập email" required />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label fw-semibold small">Mật khẩu</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
              <input v-model="form.matKhau" type="password" class="form-control border-start-0" placeholder="Nhập mật khẩu" required />
            </div>
          </div>
          <div v-if="error" class="alert alert-danger py-2 small">
            <i class="bi bi-exclamation-triangle me-1"></i>{{ error }}
          </div>
          <button type="submit" class="btn btn-primary w-100 fw-semibold py-2" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-1"></span>
            {{ loading ? 'Đang xử lý...' : 'Đăng nhập' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const form = ref({ email: '', matKhau: '' })
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''
  loading.value = true
  try {
    const res = await fetch('https://shoes-web-be-t7xh.onrender.com/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })
    if (!res.ok) { const d = await res.json().catch(() => null); throw new Error(d?.error || 'Đăng nhập thất bại') }
    const user = await res.json()
    if (user.loaiTaiKhoan !== 'ADMIN' && user.loaiTaiKhoan !== 'NHANVIEN') {
      throw new Error('Bạn không có quyền truy cập trang quản trị')
    }
    if (user.token) {
      localStorage.setItem('admin_token', user.token)
    }
    localStorage.setItem('admin_user', JSON.stringify(user))
    router.push('/')
  } catch (e) { error.value = e.message }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 50%, #1e3a5f 100%);
}
</style>
