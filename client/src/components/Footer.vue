<template>
  <footer class="footer">
    <!-- Newsletter Strip -->
    <div class="newsletter-strip">
      <div class="ns-inner">
        <div class="ns-text">
          <h3>ĐĂNG KÝ NHẬN TIN</h3>
          <p>Nhận thông tin khuyến mãi và sản phẩm mới nhất</p>
        </div>
        <div class="ns-form">
          <input type="email" placeholder="Email của bạn" />
          <button>ĐĂNG KÝ</button>
        </div>
      </div>
    </div>

    <div class="footer-top">
      <div class="ft-inner">
        <!-- Brand -->
        <div class="ft-col brand-col">
          <div class="ft-logo">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#e94560" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/><path d="M16 3H8l-2 4h12l-2-4z"/></svg>
            <span class="ft-brand">Shoes<span class="ft-accent">Shop</span></span>
          </div>
          <p class="ft-desc">Cửa hàng giày trực tuyến hàng đầu Việt Nam. Cam kết sản phẩm chính hãng, chất lượng cao, giao hàng nhanh chóng.</p>
          <div class="ft-socials">
            <a href="#" title="Facebook">
              <svg viewBox="0 0 24 24" fill="currentColor"><path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/></svg>
            </a>
            <a href="#" title="Instagram">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="20" height="20" x="2" y="2" rx="5"/><circle cx="12" cy="12" r="5"/><circle cx="17.5" cy="6.5" r="1.5" fill="currentColor" stroke="none"/></svg>
            </a>
            <a href="#" title="YouTube">
              <svg viewBox="0 0 24 24" fill="currentColor"><path d="M22.54 6.42a2.78 2.78 0 0 0-1.94-2C18.88 4 12 4 12 4s-6.88 0-8.6.46a2.78 2.78 0 0 0-1.94 2A29 29 0 0 0 1 11.75a29 29 0 0 0 .46 5.33A2.78 2.78 0 0 0 3.4 19.1c1.72.46 8.6.46 8.6.46s6.88 0 8.6-.46a2.78 2.78 0 0 0 1.94-2 29 29 0 0 0 .46-5.25 29 29 0 0 0-.46-5.43z"/><polygon points="9.75 15.02 15.5 11.75 9.75 8.48 9.75 15.02" fill="#111"/></svg>
            </a>
          </div>
        </div>

        <!-- Links -->
        <div class="ft-col">
          <h4>Về chúng tôi</h4>
          <ul>
            <li><router-link to="/">Trang chủ</router-link></li>
            <li><router-link to="/products">Cửa hàng</router-link></li>
            <li><a href="#">Giới thiệu</a></li>
            <li><a href="#">Liên hệ</a></li>
          </ul>
        </div>

        <!-- Support -->
        <div class="ft-col">
          <h4>Hỗ trợ</h4>
          <ul>
            <li><a href="#">Chính sách đổi trả</a></li>
            <li><a href="#">Hướng dẫn mua hàng</a></li>
            <li><a href="#">Vận chuyển</a></li>
            <li><a href="#">FAQ</a></li>
          </ul>
        </div>

        <!-- Contact -->
        <div class="ft-col">
          <h4>Liên hệ</h4>
          <ul class="contact-list">
            <li>
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6A19.79 19.79 0 0 1 2.12 4.18 2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.362 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.338 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
              <span>0901 234 567</span>
            </li>
            <li>
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
              <span>contact@shoesshop.vn</span>
            </li>
            <li>
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
              <span>TP. Hồ Chí Minh, Việt Nam</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="footer-bottom">
      <div class="fb-inner">
        <p>© 2026 ShoesShop. All rights reserved.</p>
        <div class="fb-links">
          <a href="#">Điều khoản</a>
          <a href="#">Bảo mật</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  background: #111;
  color: #888;
  margin-top: auto;
}

/* Newsletter */
.newsletter-strip {
  background: #e94560;
  padding: 2rem;
}
.ns-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
}
.ns-text h3 {
  color: #fff;
  font-size: 1.1rem;
  font-weight: 800;
  letter-spacing: 2px;
  margin-bottom: 0.2rem;
}
.ns-text p {
  color: rgba(255,255,255,0.8);
  font-size: 0.85rem;
}
.ns-form {
  display: flex;
  gap: 0;
}
.ns-form input {
  padding: 0.7rem 1rem;
  border: none;
  font-size: 0.88rem;
  width: 280px;
  background: rgba(255,255,255,0.95);
  color: #111;
}
.ns-form input::placeholder { color: #999; }
.ns-form input:focus { outline: none; background: #fff; }
.ns-form button {
  padding: 0.7rem 1.5rem;
  background: #111;
  color: #fff;
  border: none;
  font-weight: 800;
  font-size: 0.75rem;
  letter-spacing: 1px;
  cursor: pointer;
  transition: background 0.2s;
}
.ns-form button:hover { background: #333; }

/* Footer top */
.footer-top {
  padding: 3rem 2rem 2.5rem;
  border-top: 1px solid rgba(255,255,255,0.06);
}
.ft-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1.5fr 1fr 1fr 1fr;
  gap: 3rem;
}

/* Brand col */
.ft-logo {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-bottom: 1rem;
}
.ft-brand {
  font-size: 1.3rem;
  font-weight: 900;
  color: #fff;
  letter-spacing: -0.5px;
}
.ft-accent { color: #e94560; }
.ft-desc {
  font-size: 0.85rem;
  line-height: 1.7;
  margin-bottom: 1.2rem;
}
.ft-socials {
  display: flex;
  gap: 0.5rem;
}
.ft-socials a {
  width: 34px;
  height: 34px;
  background: rgba(255,255,255,0.08);
  border: 1px solid rgba(255,255,255,0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #888;
  transition: all 0.2s;
}
.ft-socials a svg {
  width: 15px;
  height: 15px;
}
.ft-socials a:hover {
  background: #e94560;
  border-color: #e94560;
  color: #fff;
}

/* Columns */
.ft-col h4 {
  color: #fff;
  font-size: 0.75rem;
  font-weight: 800;
  margin-bottom: 1rem;
  text-transform: uppercase;
  letter-spacing: 2px;
}
.ft-col ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.ft-col ul li {
  margin-bottom: 0.6rem;
}
.ft-col ul li a {
  color: #888;
  font-size: 0.85rem;
  transition: color 0.2s;
  display: inline-block;
  text-decoration: none;
}
.ft-col ul li a:hover {
  color: #e94560;
}

/* Contact */
.contact-list li {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}
.contact-list li svg {
  width: 15px;
  height: 15px;
  flex-shrink: 0;
  color: #e94560;
}
.contact-list li span {
  font-size: 0.85rem;
}

/* Bottom */
.footer-bottom {
  border-top: 1px solid rgba(255,255,255,0.06);
  padding: 1.2rem 2rem;
}
.fb-inner {
  max-width: 1320px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.fb-inner p {
  font-size: 0.78rem;
  color: #555;
}
.fb-links {
  display: flex;
  gap: 1.5rem;
}
.fb-links a {
  color: #555;
  font-size: 0.78rem;
  transition: color 0.2s;
  text-decoration: none;
}
.fb-links a:hover { color: #e94560; }

@media (max-width: 768px) {
  .ns-inner {
    flex-direction: column;
    text-align: center;
  }
  .ns-form input { width: 200px; }
  .ft-inner {
    grid-template-columns: 1fr 1fr;
    gap: 2rem;
  }
  .brand-col {
    grid-column: 1 / -1;
  }
  .fb-inner {
    flex-direction: column;
    gap: 0.5rem;
    text-align: center;
  }
}
@media (max-width: 480px) {
  .ft-inner {
    grid-template-columns: 1fr;
  }
  .ns-form {
    flex-direction: column;
  }
  .ns-form input { width: 100%; }
}
</style>
