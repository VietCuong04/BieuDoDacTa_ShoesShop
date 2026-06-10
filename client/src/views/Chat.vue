<template>
  <div class="chat-page">
    <aside class="history-panel">
      <div class="history-header">
        <div>
          <p class="history-title">Lịch sử chat</p>
          <p class="history-sub">Mỗi cuộc hội thoại là một mục riêng</p>
        </div>
        <div class="history-actions">
          <button class="history-new" type="button" @click="startNewChat">Mới</button>
          <button class="history-clear" type="button" @click="clearHistory">Xoá</button>
        </div>
      </div>

      <div class="history-list">
        <button
          v-for="item in historyItems"
          :key="item.id"
          type="button"
          class="history-item"
          :class="{ active: activeHistoryId === item.id }"
          @click="focusHistory(item.id)"
        >
          <span class="history-question">{{ item.title }}</span>
          <span class="history-preview">{{ item.preview }}</span>
          <span class="history-meta">{{ formatHistoryTime(item.createdAt) }}</span>
        </button>

        <div v-if="historyItems.length === 0" class="history-empty">
          Chưa có lịch sử chat.
        </div>
      </div>
    </aside>

    <div class="chat-card">
      <header class="chat-header">
        <div>
          <p class="chat-title">Chatbot tư vấn giày</p>
          <p class="chat-sub">Dùng dữ liệu shop để trả lời chính xác</p>
        </div>
      </header>

      <div class="chat-body" ref="chatBody">
        <div v-for="(m, idx) in messages" :key="m.id || idx" :id="m.id ? `msg-${m.id}` : null" :class="['bubble', m.role]">
          <span class="role">{{ m.role === 'user' ? 'Bạn' : 'Bot' }}</span>
          <div v-if="m.images && m.images.length" class="message-images">
            <figure v-for="img in m.images" :key="img.src" class="message-image-card">
              <img :src="img.src" :alt="img.alt" class="message-image" />
              <figcaption class="message-image-caption">{{ img.name }}</figcaption>
            </figure>
          </div>
          <p class="text">{{ m.text }}</p>
        </div>
        
        <!-- Loading Indicator Bubble -->
        <div v-if="loading" class="bubble assistant loading-bubble">
          <span class="role">Bot</span>
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>

        <div v-if="messages.length === 0 && !loading" class="placeholder">
          <img src="https://cdn-icons-png.flaticon.com/512/4712/4712010.png" alt="Bot Icon" class="bot-icon-placeholder" />
          <p>Hỏi về size, chất liệu, thương hiệu hoặc chính sách đổi trả...</p>
        </div>
      </div>

      <form class="chat-input" @submit.prevent="send">
        <input
          v-model="text"
          type="text"
          placeholder="Nhập câu hỏi..."
          :disabled="loading"
        />
        <button type="submit" :disabled="loading || !text.trim()">Gửi</button>
      </form>
    </div>
  </div>
</template>

<script>
const API_BASE = import.meta.env.VITE_API_URL || 'https://shoes-web-be-t7xh.onrender.com'
const CHAT_SESSIONS_KEY = 'client_chat_sessions'
const DEFAULT_SHOE_IMAGE = 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80'

export default {
  name: 'Chat',
  data() {
    return {
      text: '',
      loading: false,
      messages: [],
      historyItems: [],
      activeHistoryId: null,
      currentChatId: null,
      productCatalog: []
    }
  },
  mounted() {
    this.loadHistory()
    this.loadProductCatalog()
    this.scrollToBottom()
  },
  methods: {
    loadHistory() {
      try {
        const saved = localStorage.getItem(CHAT_SESSIONS_KEY)
        const sessions = saved ? JSON.parse(saved) : []
        this.historyItems = Array.isArray(sessions) ? sessions : []
      } catch {
        this.historyItems = []
      }

      if (this.historyItems.length > 0) {
        const latest = this.historyItems[0]
        this.currentChatId = latest.id
        this.messages = Array.isArray(latest.messages) ? latest.messages : []
        this.activeHistoryId = latest.id
      } else {
        this.startNewChat()
      }
    },
    saveHistory() {
      localStorage.setItem(CHAT_SESSIONS_KEY, JSON.stringify(this.historyItems))
    },
    ensureCurrentSession() {
      if (this.currentChatId) return this.currentChatId
      const id = `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
      const session = {
        id,
        title: 'Cuộc trò chuyện mới',
        preview: 'Chưa có nội dung',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        messages: []
      }
      this.historyItems.unshift(session)
      this.currentChatId = id
      this.activeHistoryId = id
      this.messages = session.messages
      this.saveHistory()
      return id
    },
    updateCurrentSession(meta = {}) {
      const session = this.historyItems.find((item) => item.id === this.currentChatId)
      if (!session) return
      session.messages = this.messages
      session.updatedAt = new Date().toISOString()
      if (meta.title) session.title = meta.title
      if (meta.preview) session.preview = meta.preview
      if (this.messages.length > 0) {
        const firstUser = this.messages.find((item) => item.role === 'user')
        if (firstUser) {
          session.title = session.title === 'Cuộc trò chuyện mới' ? this.shortText(firstUser.text) : session.title
          session.preview = meta.preview || this.shortText(this.messages[this.messages.length - 1]?.text || firstUser.text)
        }
      }
      this.historyItems = [session, ...this.historyItems.filter((item) => item.id !== session.id)]
    },
    async loadProductCatalog() {
      try {
        const res = await fetch(`${API_BASE}/api/products`)
        const products = await res.json()
        this.productCatalog = Array.isArray(products) ? products : []
      } catch {
        this.productCatalog = []
      }
    },
    getProductImage(product) {
      return product?.thumbImage || product?.hinhAnh || DEFAULT_SHOE_IMAGE
    },
    extractMatchedProducts(content, reply = '') {
      const text = `${content} ${reply}`.toLowerCase()
      const matched = []
      const seen = new Set()

      for (const product of this.productCatalog) {
        const name = String(product?.tenSanPham || '').trim()
        if (!name) continue
        const normalized = name.toLowerCase()
        if (text.includes(normalized) && !seen.has(product.maSanPham)) {
          seen.add(product.maSanPham)
          matched.push({
            src: this.getProductImage(product),
            alt: name,
            name
          })
        }
      }

      return matched
    },
    shouldShowShoeImage(content, reply) {
      const text = `${content} ${reply || ''}`.toLowerCase()
      return text.includes('giày') || text.includes('shoe') || text.includes('sneaker') || text.includes('dép')
    },
    shortText(text) {
      if (!text) return 'Cuộc trò chuyện mới'
      const cleaned = String(text).trim().replace(/\s+/g, ' ')
      return cleaned.length > 42 ? `${cleaned.slice(0, 42)}...` : cleaned
    },
    startNewChat() {
      const id = `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
      this.currentChatId = id
      this.activeHistoryId = id
      this.messages = []
      this.historyItems.unshift({
        id,
        title: 'Cuộc trò chuyện mới',
        preview: 'Chưa có nội dung',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        messages: []
      })
      this.saveHistory()
    },
    async send() {
      const content = this.text.trim()
      if (!content) return
      this.ensureCurrentSession()
      const turnId = `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
      this.messages.push({ role: 'user', text: content, id: `${turnId}-user`, createdAt: new Date().toISOString() })
      this.text = ''
      this.loading = true
      this.scrollToBottom()
      
      try {
        // Get user email from localStorage if logged in
        let userEmail = 'anonymous'
        try {
          const user = JSON.parse(localStorage.getItem('user') || 'null')
          if (user && user.email) {
            userEmail = user.email
          }
        } catch {
          // User not logged in, use anonymous
        }
        
        const res = await fetch(`${API_BASE}/api/chat`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ message: content, email: userEmail })
        })
        const json = await res.json()
        const reply = (json.reply || '').replaceAll('*', '') || 'Bot chưa phản hồi.'
        const matchedImages = this.extractMatchedProducts(content, reply)
        const assistantMessage = {
          role: 'assistant',
          id: `${turnId}-assistant`,
          text: reply,
          images: matchedImages.length > 0
            ? matchedImages
            : (this.shouldShowShoeImage(content, reply)
              ? [{ src: DEFAULT_SHOE_IMAGE, alt: 'Ảnh giày', name: 'Sản phẩm liên quan' }]
              : [])
        }
        this.messages.push(assistantMessage)
        this.updateCurrentSession({
          title: this.shortText(content),
          preview: this.shortText(reply)
        })
        this.saveHistory()
        this.scrollToBottom()
      } catch (err) {
        this.messages.push({ role: 'assistant', id: `${turnId}-assistant`, text: 'Hiện tại chatbot đang bận, vui lòng thử lại sau.', images: [] })
        this.updateCurrentSession({
          title: this.shortText(content),
          preview: 'Hiện tại chatbot đang bận, vui lòng thử lại sau.'
        })
        this.saveHistory()
      } finally {
        this.loading = false
      }
    },
    focusHistory(id) {
      this.activeHistoryId = id
      this.currentChatId = id
      const session = this.historyItems.find((item) => item.id === id)
      this.messages = session && Array.isArray(session.messages) ? session.messages : []
      this.$nextTick(() => {
        const el = document.getElementById(`msg-${id}-user`)
        if (el) {
          el.scrollIntoView({ behavior: 'smooth', block: 'center' })
        }
      })
    },
    clearHistory() {
      this.messages = []
      this.historyItems = []
      this.activeHistoryId = null
      this.currentChatId = null
      localStorage.removeItem(CHAT_SESSIONS_KEY)
    },
    formatHistoryTime(value) {
      if (!value) return ''
      return new Date(value).toLocaleString('vi-VN', { dateStyle: 'short', timeStyle: 'short' })
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const el = this.$refs.chatBody
        if (el) {
          el.scrollTop = el.scrollHeight
        }
      })
    }
  }
}
</script>

<style scoped>
.chat-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: stretch;
  gap: 16px;
  background: linear-gradient(135deg, #0f172a 0%, #111827 40%, #0b1224 100%);
  padding: 24px;
  color: #e5e7eb;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
}

.history-panel {
  width: 320px;
  max-width: 100%;
  background: rgba(17, 24, 39, 0.9);
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.35);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.history-header {
  padding: 16px 16px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.12);
}

.history-title {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
}

.history-sub {
  margin: 2px 0 0;
  font-size: 12px;
  color: #94a3b8;
}

.history-clear {
  border: 1px solid rgba(148, 163, 184, 0.2);
  background: transparent;
  color: #cbd5e1;
  border-radius: 999px;
  padding: 8px 12px;
  font-size: 12px;
  cursor: pointer;
}

.history-actions {
  display: flex;
  gap: 8px;
}

.history-new {
  border: 1px solid rgba(59, 130, 246, 0.35);
  background: rgba(37, 99, 235, 0.12);
  color: #bfdbfe;
  border-radius: 999px;
  padding: 8px 12px;
  font-size: 12px;
  cursor: pointer;
}

.history-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  overflow-y: auto;
  max-height: calc(100vh - 120px);
}

.history-item {
  width: 100%;
  text-align: left;
  border: 1px solid rgba(148, 163, 184, 0.14);
  background: rgba(15, 23, 42, 0.72);
  color: #e5e7eb;
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  transition: border-color 0.15s ease, transform 0.15s ease, background 0.15s ease;
}

.history-item:hover {
  transform: translateY(-1px);
  border-color: rgba(59, 130, 246, 0.4);
}

.history-item.active {
  border-color: rgba(59, 130, 246, 0.6);
  background: rgba(30, 41, 59, 0.95);
}

.history-question {
  display: block;
  font-size: 13px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 6px;
}

.history-preview {
  display: block;
  font-size: 12px;
  color: #dbeafe;
  line-height: 1.4;
  margin-bottom: 6px;
  opacity: 0.86;
}

.history-meta {
  display: block;
  font-size: 11px;
  color: #94a3b8;
}

.history-empty {
  padding: 18px 12px;
  color: #94a3b8;
  font-size: 13px;
  text-align: center;
}

.chat-card {
  width: min(900px, 100%);
  background: rgba(17, 24, 39, 0.9);
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.15);
  background: linear-gradient(120deg, rgba(59, 130, 246, 0.15), rgba(59, 130, 246, 0));
}

.chat-title {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.chat-sub {
  margin: 2px 0 0;
  color: #cbd5e1;
  font-size: 13px;
}

.typing {
  font-size: 12px;
  color: #93c5fd;
}

.chat-body {
  padding: 16px 20px;
  height: 420px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: radial-gradient(circle at 20% 20%, rgba(59, 130, 246, 0.08), transparent 35%),
              radial-gradient(circle at 80% 10%, rgba(16, 185, 129, 0.06), transparent 30%),
              rgba(17, 24, 39, 0.7);
}

.placeholder {
  text-align: center;
  color: #94a3b8;
  font-size: 14px;
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.placeholder p {
  margin: 0;
}

.bot-icon-placeholder {
  width: 60px;
  height: 60px;
  opacity: 0.6;
  margin-bottom: 12px;
  filter: grayscale(100%) invert(50%);
}

.bubble {
  max-width: 78%;
  padding: 12px 14px;
  border-radius: 12px;
  background: #1f2937;
  border: 1px solid rgba(148, 163, 184, 0.15);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 6px 4px;
  align-items: center;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background-color: #94a3b8;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.bubble .role {
  padding: 12px 10px;
  border-radius: 12px;
  background: #1f2937;
  border: 1px solid rgba(148, 163, 184, 0.15);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
}

.bubble .role {
  display: block;
  font-size: 11px;
  color: #9ca3af;
  margin-bottom: 4px;
}

.bubble .text {
  margin: 0;
  color: #e5e7eb;
  line-height: 1.5;
  white-space: pre-wrap;
}

.message-image {
  width: 100%;
  border-radius: 12px;
  display: block;
  object-fit: cover;
  aspect-ratio: 4 / 3;
}

.message-images {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(170px, 1fr));
  gap: 10px;
  margin: 8px 0 10px;
}

.message-image-card {
  margin: 0;
  padding: 8px;
  border-radius: 14px;
  background: rgba(15, 23, 42, 0.7);
  border: 1px solid rgba(148, 163, 184, 0.14);
}

.message-image-caption {
  margin-top: 8px;
  font-size: 12px;
  color: #cbd5e1;
  line-height: 1.3;
}

.bubble.user {
  margin-left: auto;
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  color: #e5e7eb;
  border-color: rgba(59, 130, 246, 0.35);
}

.bubble.user .role {
  color: rgba(229, 231, 235, 0.85);
}

.bubble.assistant {
  margin-right: auto;
}

.chat-input {
  display: flex;
  gap: 10px;
  padding: 14px 16px;
  border-top: 1px solid rgba(148, 163, 184, 0.15);
  background: rgba(17, 24, 39, 0.85);
}

.chat-input input {
  flex: 1;
  padding: 12px 14px;
  border-radius: 10px;
  border: 1px solid rgba(148, 163, 184, 0.35);
  background: #0f172a;
  color: #e5e7eb;
}

.chat-input button {
  padding: 12px 18px;
  border-radius: 10px;
  border: none;
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.1s ease, box-shadow 0.1s ease;
}

.chat-input button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.chat-input button:not(:disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 10px 25px rgba(16, 185, 129, 0.25);
}

@media (max-width: 1024px) {
  .chat-page {
    gap: 14px;
    padding: 20px;
  }
  
  .history-panel {
    width: 280px;
  }
  
  .chat-header { padding: 14px 18px; }
  .chat-title { font-size: 16px; }
  .chat-body { padding: 14px 18px; height: 400px; gap: 10px; }
  .chat-input { padding: 12px 14px; gap: 8px; }
  .chat-input input { padding: 10px 12px; }
  .chat-input button { padding: 10px 16px; }
}

@media (max-width: 900px) {
  .chat-page {
    gap: 12px;
    padding: 16px;
  }
  
  .history-panel {
    width: 240px;
    border-radius: 12px;
  }
  
  .history-header { padding: 14px; }
  .history-title { font-size: 15px; }
  .history-list { 
    padding: 10px;
    gap: 8px;
    max-height: calc(100vh - 100px);
  }
  
  .chat-card {
    border-radius: 12px;
  }
  .chat-header { 
    padding: 12px 16px;
    gap: 10px;
  }
  .chat-title { font-size: 15px; }
  .chat-sub { font-size: 12px; }
  .chat-body { 
    padding: 12px 16px;
    height: 380px;
    gap: 9px;
  }
  .chat-input { 
    padding: 11px 12px;
    gap: 8px;
  }
  .chat-input input { padding: 9px 11px; font-size: 14px; }
  .chat-input button { padding: 9px 14px; font-size: 13px; }
}

@media (max-width: 768px) {
  .chat-page {
    gap: 10px;
    padding: 12px;
    flex-direction: column;
  }
  
  .history-panel {
    width: 100%;
    max-width: 100%;
    border-radius: 10px;
  }
  
  .history-header { 
    padding: 12px;
    gap: 10px;
  }
  .history-title { font-size: 14px; }
  .history-sub { font-size: 11px; margin-top: 1px; }
  .history-clear, .history-new { 
    padding: 6px 10px;
    font-size: 11px;
    border-radius: 8px;
  }
  
  .history-list { 
    padding: 10px;
    gap: 8px;
    max-height: 200px;
  }
  .history-item { 
    padding: 10px;
    border-radius: 8px;
    border-width: 1px;
  }
  .history-question { font-size: 12px; margin-bottom: 4px; }
  .history-preview { font-size: 11px; margin-bottom: 4px; }
  .history-meta { font-size: 10px; }
  .history-empty { padding: 14px 10px; font-size: 12px; }
  
  .chat-card {
    width: 100%;
    border-radius: 10px;
    min-height: 50vh;
  }
  
  .chat-header { 
    padding: 11px 14px;
    gap: 8px;
  }
  .chat-title { font-size: 14px; }
  .chat-sub { font-size: 11px; }
  .typing { font-size: 11px; }
  
  .chat-body { 
    padding: 11px 14px;
    height: 350px;
    gap: 8px;
  }
  
  .placeholder { 
    font-size: 13px;
    padding: 30px 0;
  }
  .bot-icon-placeholder { 
    width: 50px;
    height: 50px;
    margin-bottom: 10px;
  }
  
  .bubble { 
    max-width: 85%;
    padding: 10px 12px;
    border-radius: 10px;
    font-size: 13px;
  }
  .bubble .role { 
    padding: 10px 8px;
    margin-bottom: 3px;
    font-size: 10px;
  }
  .bubble .text { font-size: 13px; }
  
  .message-images {
    grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
    gap: 8px;
    margin: 6px 0 8px;
  }
  .message-image-card { padding: 6px; border-radius: 10px; }
  .message-image-caption { 
    margin-top: 6px;
    font-size: 11px;
  }
  
  .chat-input { 
    padding: 10px;
    gap: 7px;
    border-top-width: 1px;
  }
  .chat-input input { 
    padding: 8px 10px;
    font-size: 13px;
    border-radius: 8px;
  }
  .chat-input button { 
    padding: 8px 12px;
    font-size: 12px;
    border-radius: 8px;
  }
}

@media (max-width: 640px) {
  .chat-page {
    gap: 8px;
    padding: 10px;
    flex-direction: column;
    min-height: 100vh;
  }

  .history-panel {
    width: 100%;
    max-height: 150px;
    border-radius: 8px;
  }
  
  .history-header { 
    padding: 10px;
    gap: 8px;
  }
  .history-title { font-size: 13px; }
  .history-clear, .history-new { 
    padding: 5px 8px;
    font-size: 10px;
  }

  .history-list {
    padding: 8px;
    gap: 6px;
    max-height: 120px;
  }
  
  .history-item { 
    padding: 8px;
    font-size: 12px;
  }
  .history-question { font-size: 11px; margin-bottom: 3px; }
  .history-preview { font-size: 10px; }

  .chat-card {
    width: 100%;
    height: calc(100vh - 180px);
    border-radius: 8px;
  }
  
  .chat-header { 
    padding: 10px 12px;
    gap: 7px;
  }
  .chat-title { font-size: 13px; }
  .chat-sub { font-size: 10px; }
  
  .chat-body {
    padding: 10px 12px;
    height: 100%;
    gap: 7px;
  }
  
  .placeholder { 
    font-size: 12px;
    padding: 20px 0;
  }
  .bot-icon-placeholder { 
    width: 45px;
    height: 45px;
    margin-bottom: 8px;
  }
  
  .bubble { 
    max-width: 88%;
    padding: 9px 11px;
    font-size: 12px;
  }
  .bubble .role { 
    padding: 8px 6px;
    margin-bottom: 2px;
    font-size: 9px;
  }
  
  .message-images {
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 6px;
    margin: 5px 0 6px;
  }
  .message-image-card { padding: 5px; }
  .message-image-caption { font-size: 10px; margin-top: 5px; }
  
  .chat-input { 
    padding: 9px;
    gap: 6px;
  }
  .chat-input input { 
    padding: 7px 9px;
    font-size: 12px;
  }
  .chat-input button { 
    padding: 7px 10px;
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .chat-page {
    gap: 6px;
    padding: 8px;
  }

  .history-panel {
    max-height: 120px;
    border-radius: 6px;
  }
  
  .history-header { 
    padding: 8px;
    gap: 6px;
  }
  .history-title { font-size: 12px; }
  .history-clear, .history-new { 
    padding: 4px 6px;
    font-size: 9px;
  }

  .history-list {
    padding: 6px;
    gap: 5px;
    max-height: 100px;
  }
  
  .history-item { 
    padding: 6px;
    font-size: 11px;
  }
  .history-question { font-size: 10px; margin-bottom: 2px; }
  .history-preview { font-size: 9px; }
  .history-meta { font-size: 9px; }

  .chat-card {
    height: calc(100vh - 160px);
    border-radius: 6px;
  }
  
  .chat-header { 
    padding: 8px 10px;
    gap: 6px;
  }
  .chat-title { font-size: 12px; }
  .chat-sub { font-size: 9px; }
  
  .chat-body {
    padding: 8px 10px;
    gap: 6px;
  }
  
  .placeholder { 
    font-size: 11px;
    padding: 15px 0;
  }
  .bot-icon-placeholder { 
    width: 40px;
    height: 40px;
    margin-bottom: 6px;
  }
  
  .bubble { 
    max-width: 90%;
    padding: 8px 10px;
    font-size: 11px;
  }
  .bubble .role { 
    padding: 6px 5px;
    margin-bottom: 1px;
    font-size: 8px;
  }
  
  .message-images {
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
    gap: 5px;
    margin: 4px 0 5px;
  }
  .message-image-card { padding: 4px; border-radius: 6px; }
  .message-image-caption { font-size: 9px; }
  
  .chat-input { 
    padding: 8px;
    gap: 5px;
  }
  .chat-input input { 
    padding: 6px 8px;
    font-size: 11px;
  }
  .chat-input button { 
    padding: 6px 8px;
    font-size: 10px;
  }
}
</style>
