import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router)
app.mount('#app')


// ### Backend (be)
// - Chưa có spring-boot-starter-security và cấu hình Spring Security / JWT.
// - Bảo mật hiện tại chỉ dựa trên header/`localStorage`, chưa đủ an toàn.
// - Chưa có endpoint báo cáo chatbot: lượt chat, tỉ lệ chuyển đổi chat -> đơn hàng, doanh thu từ chat.

// ### Admin App (admin)
// - Phân quyền admin hiện chỉ kiểm tra phía client, server-side chưa bảo vệ.
// - Dashboard chưa hiển thị số liệu chat và chuyển đổi từ chatbot.

// ### Client App (client)
// - Chưa có lưu lịch sử chat và báo cáo chuyển đổi từ chat.