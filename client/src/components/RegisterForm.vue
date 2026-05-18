<template>
  <div class="auth-container">
    <div class="auth-box">
      <h2>Đăng ký</h2>
      <form @submit.prevent="handleRegister">
        <input v-model="form.tenDangNhap" type="text" placeholder="Tên đăng nhập" required />
        <input v-model="form.tenNguoiDung" type="text" placeholder="Tên sử dụng" required />
        <input v-model="form.matKhau" type="password" placeholder="Mật khẩu" required />
        <button type="submit">Đăng ký ngay</button>
      </form>
      <p class="switch-link">Bạn đã có tài khoản? <router-link to="/login">Đăng nhập ngay</router-link></p>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  tenDangNhap: '',
  tenNguoiDung: '',
  matKhau: ''
})
const error = ref('')

async function handleRegister() {
  error.value = ''
  try {
    const res = await fetch('/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    if (!res.ok) {
      const msg = await res.text()
      throw new Error(msg)
    }
    router.push('/login')
  } catch (e) {
    error.value = e.message
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}
.auth-box {
  background: #fff;
  padding: 2rem 2.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.08);
  min-width: 320px;
}
h2 {
  text-align: center;
  margin-bottom: 1.5rem;
}
input {
  width: 100%;
  padding: 0.7rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  width: 100%;
  padding: 0.7rem;
  background: #222;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-weight: 600;
  cursor: pointer;
}
.switch-link {
  text-align: center;
  margin-top: 1rem;
}
.error {
  color: #d00;
  text-align: center;
  margin-top: 0.5rem;
}
</style>
