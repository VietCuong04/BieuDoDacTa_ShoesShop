# Tổng hợp đánh giá đồ án bán giày trực tuyến

## 1. Hoàn thành

### 1.1 Frontend
- Có website client Vue.js với:
  - trang danh sách sản phẩm
  - trang chi tiết sản phẩm
  - giỏ hàng
  - checkout mô phỏng
  - đăng nhập / đăng ký
  - trang chat chatbot với lịch sử chat cục bộ
- Có website admin Vue.js với:
  - dashboard quản trị
  - quản lý sản phẩm, chất liệu, size, thương hiệu, xuất xứ, màu sắc
  - quản lý đơn hàng, khách hàng, khuyến mãi
  - phân quyền route admin và login admin

### 1.2 Backend
- Đã triển khai Spring Boot với REST API cho các thực thể chính
- Đã thiết lập JWT authentication cơ bản với `SecurityConfig` và `JwtFilter`
- Có endpoint auth `/api/auth/register`, `/api/auth/login`
- Có endpoint chat `/api/chat` và thống kê chat `/api/chat/stats`
- Có lưu log chat vào bảng `chat_log`

### 1.3 Chatbot
- Tích hợp chatbot sử dụng LLM với prompt được xây dựng từ dữ liệu sản phẩm nội bộ
- Chatbot có thể trả lời theo nội dung sản phẩm, size, chất liệu, xuất xứ
- Chatbot đã hỗ trợ trả lời các câu hỏi về chính sách đổi trả / mua hàng của shop
- Chatbot đã sử dụng dữ liệu nội bộ phù hợp với đề cương: sản phẩm, size, chính sách, FAQ
- Có cơ chế fallback khi LLM không trả lời được

### 1.4 Bảo mật và phân quyền
- Đã triển khai xác thực JWT cho backend như yêu cầu cơ bản
- Có cấu hình CORS phù hợp cho frontend và API
- Đã phân quyền admin cho các route quản lý chính
- Với phạm vi đồ án học phần, mức bảo mật này là hợp lý và đáp ứng yêu cầu cơ bản

### 1.5 Khớp với đề cương
- Đề cương yêu cầu chatbot tư vấn sản phẩm và giải đáp chính sách đổi trả: phần này đã có
- Đề cương yêu cầu dữ liệu nội bộ (RAG / dataset shop): phần nội dung này đã được tích hợp thông qua prompt và dữ liệu sản phẩm + chính sách
- Đề cương yêu cầu báo cáo phân tích hiệu quả chatbot: đã có số liệu chat cơ bản

## 2. Chưa hoàn thành / cần hoàn thiện

### 2.1 Phân tích, đo lường chat
- Chưa có liên kết chat session với đơn hàng để đánh giá mức độ ảnh hưởng của chat
- Chưa có thống kê “tỷ lệ khách đặt hàng sau khi chat”
- Chưa có báo cáo rõ ràng về tác động chat tới hành vi mua hàng

### 2.2 Tài liệu và báo cáo
- Cần hoàn thiện tài liệu API, ERD và mô tả rõ phần đã làm / phần đang thiếu
- Cần ghi rõ trong đồ án rằng chatbot tư vấn sản phẩm, chưa hỗ trợ đặt hàng trực tiếp

## 3. Kết luận
- Dự án đã hoàn thành phần lớn yêu cầu chính: frontend, backend, chatbot tư vấn, và chính sách đổi trả/FAQ.
- Phần cần hoàn thiện chủ yếu là đo lường hiệu quả chat và liên kết chat với hành vi đặt hàng.
- Với cách trình bày đúng hiện trạng, dự án có thể đạt điểm tốt nếu làm rõ phần hoàn thành và phần mở rộng.
