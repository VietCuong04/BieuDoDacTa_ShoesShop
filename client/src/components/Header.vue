<template>
  <header class="header" :class="{ scrolled: isScrolled }">
    <div class="header-inner">
      <router-link to="/" class="logo">
        <span class="logo-icon">👟</span>
        <span class="logo-text">Shoes<span class="logo-accent">Shop</span></span>
      </router-link>

      <nav class="nav">
        <router-link to="/" exact-active-class="active">Trang chủ</router-link>
        <router-link to="/products" active-class="active">Sản phẩm</router-link>
        <router-link to="/chat" active-class="active">Chat</router-link>
      </nav>

      <div class="actions">
        <div class="search-wrap" :class="{ expanded: searchFocused }">
          <svg class="search-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
          <input
            type="text"
            placeholder="Tìm kiếm..."
            @focus="searchFocused = true"
            @blur="searchFocused = false"
          />
        </div>

        <!-- Cart Icon -->
        <router-link to="/cart" class="cart-link" title="Giỏ hàng">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
          </svg>
          <span class="cart-badge" v-if="cartCount > 0">{{ cartCount }}</span>
        </router-link>

        <template v-if="user">
          <router-link to="/profile" class="user-pill" title="Tài khoản">
            <div class="avatar">{{ user.tenNguoiDung?.charAt(0)?.toUpperCase() }}</div>
             <span class="user-label">{{ shortUserName(user.tenNguoiDung) }}</span>
          </router-link>
          <button class="btn-out" @click="logout">Đăng xuất</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn-login">Đăng nhập</router-link>
          <router-link to="/register" class="btn-signup">Đăng ký</router-link>
        </template>
      </div>

      <!-- Mobile menu toggle -->
      <button class="mobile-toggle" @click="mobileOpen = !mobileOpen">
        <span :class="{ open: mobileOpen }"></span>
      </button>
    </div>

    <!-- Mobile menu -->
    <div class="mobile-menu" v-if="mobileOpen" @click="mobileOpen = false">
      <router-link to="/">Trang chủ</router-link>
      <router-link to="/products">Sản phẩm</router-link>
      <router-link to="/cart">Giỏ hàng ({{ cartCount }})</router-link>
      <router-link to="/chat">Chat</router-link>
      <router-link to="/profile" v-if="user">Tài khoản</router-link>
      <router-link to="/login" v-if="!user">Đăng nhập</router-link>
      <router-link to="/register" v-if="!user">Đăng ký</router-link>
      <a href="#" v-if="user" @click.prevent="logout">Đăng xuất</a>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const user = ref(null)
const cartCount = ref(0)
const searchFocused = ref(false)
const isScrolled = ref(false)
const mobileOpen = ref(false)

function loadUser() {
  const saved = localStorage.getItem('user')
  user.value = saved ? JSON.parse(saved) : null
}

function loadCartCount() {
  const saved = localStorage.getItem('cart')
  const cart = saved ? JSON.parse(saved) : []
  cartCount.value = cart.reduce((s, i) => s + (i.soLuong || 0), 0)
}

function shortUserName(name) {
  if (!name) return ''
  const cleaned = String(name).trim().replace(/\s+/g, ' ')
  return cleaned.length > 12 ? `${cleaned.slice(0, 12)}...` : cleaned
}

function onScroll() {
  isScrolled.value = window.scrollY > 20
}

function onUserChanged() {
  loadUser()
}

function onCartChanged() {
  loadCartCount()
}

// Re-check user on every route change
watch(() => route.path, () => {
  loadUser()
  loadCartCount()
})

onMounted(() => {
  loadUser()
  loadCartCount()
  window.addEventListener('scroll', onScroll)
  window.addEventListener('user-changed', onUserChanged)
  window.addEventListener('cart-changed', onCartChanged)
})
onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
  window.removeEventListener('user-changed', onUserChanged)
  window.removeEventListener('cart-changed', onCartChanged)
})

function logout() {
  localStorage.removeItem('user')
  user.value = null
  window.dispatchEvent(new Event('user-changed'))
  router.push('/login')
}
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 200;
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0,0,0,0.06);
  transition: box-shadow 0.3s, background 0.3s;
}
.header.scrolled {
  background: rgba(255,255,255,0.95);
  box-shadow: 0 1px 20px rgba(0,0,0,0.08);
}
.header-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 68px;
  padding: 0 2rem;
  gap: 2rem;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  flex-shrink: 0;
}
.logo-icon { font-size: 1.6rem; }
.logo-text {
  font-size: 1.35rem;
  font-weight: 800;
  color: #1a1a2e;
  letter-spacing: -0.5px;
}
.logo-accent { color: #e94560; }

/* Nav */
.nav {
  display: flex;
  gap: 0.5rem;
}
.nav a {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  font-weight: 500;
  color: #666;
  border-radius: 8px;
  transition: all 0.2s;
}
.nav a:hover { color: #1a1a2e; background: #f0f0f0; }
.nav a.active { color: #e94560; background: rgba(233,69,96,0.08); font-weight: 600; }

/* Actions */
.actions {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-left: auto;
}

/* Search */
.search-wrap {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  background: #f0f0f0;
  border-radius: 10px;
  padding: 0 0.8rem;
  height: 38px;
  transition: all 0.3s;
  width: 180px;
  border: 1.5px solid transparent;
}
.search-wrap.expanded {
  width: 260px;
  background: #fff;
  border-color: #e94560;
  box-shadow: 0 0 0 3px rgba(233,69,96,0.1);
}
.search-svg {
  width: 16px;
  height: 16px;
  color: #999;
  flex-shrink: 0;
}
.search-wrap input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 0.85rem;
  color: #333;
  width: 100%;
}
.search-wrap input::placeholder { color: #aaa; }

/* Cart */
.cart-link {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  color: #555;
  transition: color 0.2s;
}
.cart-link:hover { color: #e94560; }
.cart-badge {
  position: absolute;
  top: 2px;
  right: 0;
  background: #e94560;
  color: #fff;
  font-size: 0.65rem;
  font-weight: 700;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
}

/* User */
.user-pill {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  background: #f0f0f0;
  padding: 0.3rem 0.8rem 0.3rem 0.3rem;
  border-radius: 50px;
  text-decoration: none;
  transition: background 0.2s;
  padding-right: 1rem;
}
.user-pill:hover { background: #e8e8e8; }
.avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e94560, #c0392b);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 700;
}
.user-label {
  font-size: 0.82rem;
  font-weight: 600;
  color: #333;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Buttons */
.btn-login {
  font-size: 0.85rem;
  font-weight: 600;
  color: #1a1a2e;
  padding: 0.45rem 1.1rem;
  border-radius: 8px;
  transition: background 0.2s;
}
.btn-login:hover { background: #f0f0f0; }
.btn-signup {
  font-size: 0.85rem;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #e94560, #d63851);
  padding: 0.45rem 1.3rem;
  border-radius: 8px;
  transition: transform 0.15s, box-shadow 0.2s;
}
.btn-signup:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(233,69,96,0.35);
}
.btn-out {
  font-size: 0.82rem;
  font-weight: 600;
  color: #e94560;
  background: transparent;
  border: 1.5px solid #e94560;
  padding: 0.35rem 0.9rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-out:hover { background: #e94560; color: #fff; }

/* Mobile */
.mobile-toggle { 
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin-left: auto;
}
.mobile-menu { display: none; }

@media (max-width: 900px) {
  .search-wrap { display: none; }
  .header-inner { padding: 0 1.5rem; }
  .logo-text { font-size: 1.2rem; }
}

@media (max-width: 768px) {
  .header-inner {
    height: 60px;
    padding: 0 1rem;
    gap: 0.8rem;
  }
  .logo-icon { font-size: 1.35rem; }
  .logo-text { font-size: 1.1rem; }
  .nav { display: none; }
  .search-wrap { display: none !important; }
  
  .actions {
    gap: 0.4rem;
  }
  .cart-link { width: 36px; height: 36px; }
  .cart-badge { width: 16px; height: 16px; font-size: 0.6rem; top: 0; right: -2px; }
  
  .user-pill {
    padding: 0.25rem 0.6rem 0.25rem 0.25rem;
    padding-right: 0.8rem;
    gap: 0.3rem;
  }
  .avatar { width: 26px; height: 26px; font-size: 0.7rem; }
  .user-label { 
    font-size: 0.75rem;
    max-width: 100px;
  }
  
  .btn-login { 
    padding: 0.35rem 0.9rem;
    font-size: 0.8rem;
  }
  .btn-signup { 
    padding: 0.35rem 1rem;
    font-size: 0.8rem;
  }
  .btn-out { 
    padding: 0.3rem 0.8rem;
    font-size: 0.75rem;
  }
  
  .mobile-toggle {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px; height: 32px;
    margin-left: 0.3rem;
  }
  .mobile-toggle span,
  .mobile-toggle span::before,
  .mobile-toggle span::after {
    display: block;
    width: 20px;
    height: 2.3px;
    background: #333;
    border-radius: 2px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
  }
  .mobile-toggle span::before,
  .mobile-toggle span::after {
    content: '';
    position: absolute;
    left: 0;
  }
  .mobile-toggle span::before { top: -7px; }
  .mobile-toggle span::after { top: 7px; }
  .mobile-toggle span.open { background: transparent; }
  .mobile-toggle span.open::before { top: 0; transform: rotate(45deg); }
  .mobile-toggle span.open::after { top: 0; transform: rotate(-45deg); }
  .mobile-menu {
    display: flex;
    flex-direction: column;
    background: #fff;
    border-top: 1px solid #eee;
    padding: 0.5rem 0;
    max-height: calc(100vh - 60px);
    overflow-y: auto;
  }
  .mobile-menu a {
    padding: 0.65rem 1.2rem;
    font-size: 0.85rem;
    font-weight: 500;
    color: #333;
    transition: background 0.15s;
    border-bottom: 1px solid #f5f5f5;
  }
  .mobile-menu a:last-child {
    border-bottom: none;
  }
  .mobile-menu a:active,
  .mobile-menu a:hover { 
    background: #f5f5f5; 
  }
  .mobile-menu a.active {
    color: #e94560;
    background: rgba(233, 69, 96, 0.05);
  }
}

@media (max-width: 640px) {
  .header-inner {
    height: 56px;
    padding: 0 0.8rem;
    gap: 0.6rem;
  }
  .logo-icon { font-size: 1.25rem; }
  .logo-text { font-size: 1rem; }
  .logo-accent { font-size: 0.95rem; }
  
  .actions { gap: 0.35rem; }
  .cart-link { width: 34px; height: 34px; }
  .cart-badge { width: 15px; height: 15px; font-size: 0.55rem; }
  
  .user-pill {
    padding: 0.2rem 0.5rem 0.2rem 0.2rem;
    padding-right: 0.7rem;
  }
  .avatar { width: 24px; height: 24px; font-size: 0.65rem; }
  .user-label { 
    font-size: 0.7rem;
    max-width: 80px;
  }
  
  .btn-login { 
    padding: 0.3rem 0.8rem;
    font-size: 0.75rem;
  }
  .btn-signup { 
    padding: 0.3rem 0.9rem;
    font-size: 0.75rem;
  }
  .btn-out { 
    padding: 0.25rem 0.7rem;
    font-size: 0.7rem;
  }
  
  .mobile-toggle {
    width: 30px; height: 30px;
    margin-left: 0.2rem;
  }
  .mobile-toggle span,
  .mobile-toggle span::before,
  .mobile-toggle span::after {
    width: 18px;
    height: 2px;
  }
  .mobile-toggle span::before { top: -6px; }
  .mobile-toggle span::after { top: 6px; }
  
  .mobile-menu {
    max-height: calc(100vh - 56px);
  }
  .mobile-menu a {
    padding: 0.6rem 1rem;
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .header-inner {
    height: 52px;
    padding: 0 0.6rem;
    gap: 0.5rem;
  }
  .logo-icon { font-size: 1.1rem; }
  .logo-text { font-size: 0.9rem; }
  
  .actions { gap: 0.3rem; }
  .cart-link { width: 32px; height: 32px; }
  .cart-badge { width: 14px; height: 14px; font-size: 0.5rem; top: -1px; right: -3px; }
  
  .user-pill {
    padding: 0.15rem 0.4rem 0.15rem 0.15rem;
    padding-right: 0.6rem;
    display: none;
  }
  
  .btn-login { 
    padding: 0.25rem 0.7rem;
    font-size: 0.7rem;
    display: none;
  }
  .btn-signup { 
    padding: 0.25rem 0.8rem;
    font-size: 0.7rem;
  }
  .btn-out { 
    padding: 0.2rem 0.6rem;
    font-size: 0.65rem;
  }
  
  .mobile-toggle {
    width: 28px; height: 28px;
    margin-left: 0;
  }
  .mobile-toggle span,
  .mobile-toggle span::before,
  .mobile-toggle span::after {
    width: 16px;
    height: 1.8px;
  }
  .mobile-toggle span::before { top: -5px; }
  .mobile-toggle span::after { top: 5px; }
  
  .mobile-menu {
    max-height: calc(100vh - 52px);
  }
  .mobile-menu a {
    padding: 0.55rem 0.9rem;
    font-size: 0.75rem;
  }
}
</style>
