<script setup>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
</script>

<template>
  <div id="layout">
    <Header />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
    <router-link to="/chat" class="chat-fab" title="Chat với shop">
      <span class="chat-icon">💬</span>
      <span class="chat-label">Chat</span>
    </router-link>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: #fafafa;
  color: #2d2d2d;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
#layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.main-content {
  flex: 1;
}
a {
  color: inherit;
  text-decoration: none;
}
img {
  max-width: 100%;
}
button {
  font-family: inherit;
}
input, select, textarea {
  font-family: inherit;
}
::-webkit-scrollbar { width: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #ccc; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #aaa; }

.chat-fab {
  position: fixed;
  right: 22px;
  bottom: 22px;
  z-index: 250;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 14px;
  background: linear-gradient(135deg, #10b981, #059669);
  color: #fff;
  border-radius: 999px;
  box-shadow: 0 12px 30px rgba(16, 185, 129, 0.35);
  font-weight: 700;
  letter-spacing: 0.2px;
  transition: transform 0.15s ease, box-shadow 0.15s ease, opacity 0.2s;
}

.chat-fab:hover {
  transform: translateY(-1px);
  box-shadow: 0 16px 34px rgba(16, 185, 129, 0.4);
}

.chat-icon { font-size: 18px; }
.chat-label { font-size: 14px; }

@media (max-width: 640px) {
  .chat-fab {
    padding: 12px;
    border-radius: 50%;
  }
  .chat-label { display: none; }
}
</style>
