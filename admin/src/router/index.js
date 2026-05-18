import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Dashboard from '@/views/Dashboard.vue'
import ThuongHieu from '@/views/ThuongHieu.vue'
import ChatLieu from '@/views/ChatLieu.vue'
import XuatXu from '@/views/XuatXu.vue'
import MauSac from '@/views/MauSac.vue'
import SizeView from '@/views/SizeView.vue'
import SanPham from '@/views/SanPham.vue'
import DonHang from '@/views/DonHang.vue'
import NhanVien from '@/views/NhanVien.vue'
import GiamGia from '@/views/GiamGia.vue'
import KhachHang from '@/views/KhachHang.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  { path: '/', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/brands', name: 'ThuongHieu', component: ThuongHieu, meta: { requiresAuth: true } },
  { path: '/materials', name: 'ChatLieu', component: ChatLieu, meta: { requiresAuth: true } },
  { path: '/origins', name: 'XuatXu', component: XuatXu, meta: { requiresAuth: true } },
  { path: '/colors', name: 'MauSac', component: MauSac, meta: { requiresAuth: true } },
  { path: '/sizes', name: 'Size', component: SizeView, meta: { requiresAuth: true } },
  { path: '/products', name: 'SanPham', component: SanPham, meta: { requiresAuth: true } },
  { path: '/orders', name: 'DonHang', component: DonHang, meta: { requiresAuth: true } },
  { path: '/discounts', name: 'GiamGia', component: GiamGia, meta: { requiresAuth: true } },
  { path: '/employees', name: 'NhanVien', component: NhanVien, meta: { requiresAuth: true } },
  { path: '/customers', name: 'KhachHang', component: KhachHang, meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

function getAdminUser() {
  try {
    return JSON.parse(localStorage.getItem('admin_user') || 'null')
  } catch {
    return null
  }
}

function hasAdminAccess(user) {
  return user && (user.loaiTaiKhoan === 'ADMIN' || user.loaiTaiKhoan === 'NHANVIEN')
}

router.beforeEach((to, from, next) => {
  const user = getAdminUser()

  if (to.name === 'Login' && hasAdminAccess(user)) {
    next('/')
    return
  }

  if (to.meta.requiresAuth && !hasAdminAccess(user)) {
    next('/login')
  } else {
    next()
  }
})

export default router
