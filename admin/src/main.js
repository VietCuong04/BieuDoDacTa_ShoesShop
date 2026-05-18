import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

const app = createApp(App)
app.use(router)
app.mount('#app')



// ### Backend (be)
// - Đã có spring-boot-starter-security và cấu hình Spring Security / JWT.
// - Bảo mật hiện tại dựa trên JWT ở backend, frontend chỉ giữ token trong `localStorage` để gửi request.
// - Chưa có endpoint báo cáo chatbot: lượt chat, tỉ lệ chuyển đổi chat -> đơn hàng, doanh thu từ chat.

// ### Admin App (admin)
// - Phân quyền admin đã có route guard phía client; backend vẫn phải kiểm tra quyền theo JWT.
// - Dashboard chưa hiển thị số liệu chat và chuyển đổi từ chatbot.

// ### Client App (client)
// - Chưa có lưu lịch sử chat và báo cáo chuyển đổi từ chat.