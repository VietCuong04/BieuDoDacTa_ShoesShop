<template>
  <div class="chat-page">
    <div class="chat-card">
      <header class="chat-header">
        <div>
          <p class="chat-title">Chatbot tư vấn giày</p>
          <p class="chat-sub">Dùng dữ liệu shop để trả lời chính xác</p>
        </div>
      </header>

      <div class="chat-body" ref="chatBody">
        <div v-for="(m, idx) in messages" :key="idx" :class="['bubble', m.role]">
          <span class="role">{{ m.role === 'user' ? 'Bạn' : 'Bot' }}</span>
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
const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export default {
  name: 'Chat',
  data() {
    return {
      text: '',
      loading: false,
      messages: []
    }
  },
  methods: {
    async send() {
      const content = this.text.trim()
      if (!content) return
      this.messages.push({ role: 'user', text: content })
      this.text = ''
      this.loading = true
      this.scrollToBottom()
      
      try {
        const res = await fetch(`${API_BASE}/api/chat`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ message: content })
        })
        const json = await res.json()
        this.messages.push({ role: 'assistant', text: json.reply.replaceAll('*', '') || 'Bot chưa phản hồi.' })
        this.scrollToBottom()
      } catch (err) {
        this.messages.push({ role: 'assistant', text: 'Không gọi được chatbot, thử lại sau.' })
      } finally {
        this.loading = false
      }
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
  align-items: center;
  background: linear-gradient(135deg, #0f172a 0%, #111827 40%, #0b1224 100%);
  padding: 24px;
  color: #e5e7eb;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
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
  max-width: 78%;
  padding: 12px 14px;
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

@media (max-width: 640px) {
  .chat-card {
    height: 100vh;
  }
  .chat-body {
    height: 100%;
  }
}
</style>
