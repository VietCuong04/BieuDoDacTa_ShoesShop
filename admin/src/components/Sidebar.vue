<template>
  <aside class="sidebar">
    <div class="sidebar-brand">
      <i class="bi bi-shop fs-4"></i>
      <span>ShoesShop</span>
    </div>
    <nav class="sidebar-nav">
      <router-link to="/" class="nav-link">
        <i class="bi bi-grid-1x2"></i> Dashboard
      </router-link>

      <div class="nav-section">Sản phẩm</div>
      <router-link to="/products" class="nav-link">
        <i class="bi bi-box-seam"></i> Sản phẩm
      </router-link>

      <div class="nav-section">Thuộc tính</div>
      <router-link to="/brands" class="nav-link">
        <i class="bi bi-bookmark-star"></i> Thương hiệu
      </router-link>
      <router-link to="/materials" class="nav-link">
        <i class="bi bi-layers"></i> Chất liệu
      </router-link>
      <router-link to="/origins" class="nav-link">
        <i class="bi bi-globe2"></i> Xuất xứ
      </router-link>
      <router-link to="/colors" class="nav-link">
        <i class="bi bi-palette"></i> Màu sắc
      </router-link>
      <router-link to="/sizes" class="nav-link">
        <i class="bi bi-rulers"></i> Size
      </router-link>

      <div class="nav-section">Kinh doanh</div>
      <router-link to="/orders" class="nav-link">
        <i class="bi bi-receipt"></i> Đơn hàng
      </router-link>
      <router-link to="/discounts" class="nav-link">
        <i class="bi bi-ticket-perforated"></i> Giảm giá
      </router-link>

      <div class="nav-section">Tài khoản</div>
      <router-link to="/customers" class="nav-link">
        <i class="bi bi-person-lines-fill"></i> Khách hàng
      </router-link>

      <router-link to="/employees" class="nav-link">
        <i class="bi bi-people"></i> Nhân viên
      </router-link>
    </nav>
    <div class="sidebar-footer">
      <div class="user-badge">
        <i class="bi bi-person-circle"></i>
        <div>
          <div class="user-name">{{ userName }}</div>
          <div class="user-role">{{ userRole }}</div>
        </div>
      </div>
      <button @click="logout" class="btn-logout" title="Đăng xuất">
        <i class="bi bi-box-arrow-right"></i>
      </button>
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = computed(() => JSON.parse(localStorage.getItem('admin_user') || 'null'))
const userName = computed(() => user.value?.tenNguoiDung || '')
const userRole = computed(() => {
  const role = user.value?.loaiTaiKhoan
  if (role === 'ADMIN') return 'Quản trị viên'
  if (role === 'NHANVIEN') return 'Nhân viên'
  return role || ''
})

function logout() {
  localStorage.removeItem('admin_user')
  router.push('/login')
}
</script>

<style scoped>
.sidebar {
  width: 260px;
  background: #1e293b;
  color: #e2e8f0;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  display: flex;
  flex-direction: column;
  z-index: 100;
}
.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  padding: 1.25rem 1.5rem;
  border-bottom: 1px solid rgba(255,255,255,0.07);
  font-size: 1.15rem;
  font-weight: 700;
  color: #fff;
  letter-spacing: -0.3px;
}
.sidebar-nav {
  flex: 1;
  padding: 0.75rem 0;
  overflow-y: auto;
}
.nav-section {
  padding: 1rem 1.5rem 0.35rem;
  font-size: 0.65rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1.2px;
  color: #64748b;
}
.nav-link {
  display: flex;
  align-items: center;
  gap: 0.7rem;
  padding: 0.55rem 1.5rem;
  color: #94a3b8;
  text-decoration: none;
  font-size: 0.87rem;
  font-weight: 500;
  transition: all 0.15s;
  border-left: 3px solid transparent;
}
.nav-link:hover {
  background: rgba(255,255,255,0.04);
  color: #e2e8f0;
}
.nav-link.router-link-exact-active,
.nav-link.router-link-active {
  background: rgba(99, 102, 241, 0.12);
  color: #818cf8;
  border-left-color: #818cf8;
  font-weight: 600;
}
.nav-link i {
  font-size: 1.05rem;
  width: 20px;
  text-align: center;
}
.sidebar-footer {
  padding: 1rem 1.25rem;
  border-top: 1px solid rgba(255,255,255,0.07);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
}
.user-badge {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  min-width: 0;
}
.user-badge i {
  font-size: 1.6rem;
  color: #64748b;
  flex-shrink: 0;
}
.user-name {
  font-size: 0.82rem;
  font-weight: 600;
  color: #e2e8f0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 130px;
}
.user-role {
  font-size: 0.68rem;
  color: #64748b;
}
.btn-logout {
  background: none;
  border: 1px solid rgba(255,255,255,0.1);
  color: #94a3b8;
  width: 34px;
  height: 34px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.15s;
  flex-shrink: 0;
}
.btn-logout:hover {
  background: #ef4444;
  border-color: #ef4444;
  color: #fff;
}
</style>
