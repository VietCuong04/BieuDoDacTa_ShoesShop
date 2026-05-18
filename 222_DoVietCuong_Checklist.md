# Checklist:222_Phát triển hệ thống bán giày trực tuyến tích hợp chatbot tư vấn cá nhân hóa.
# Công việc đã làm và chưa làm

## 1. Việc đã làm
- Backend `be` đã có REST API CRUD cơ bản cho sản phẩm, đơn hàng, khách hàng, tài khoản.
- Chatbot backend đã được tích hợp với prompt, nhận yêu cầu và trả lời.
- Admin `admin` đã có giao diện Vue 3 + Vite, login, dashboard và các trang quản lý sản phẩm, khách hàng, đơn hàng, khuyến mãi, thương hiệu, màu sắc, kích thước, xuất xứ.
- Client `client` đã có giao diện Vue 3 + Vite, các trang Home, Products, ProductDetail, Login, Register, Cart, Checkout, Chat.
- Giỏ hàng hoạt động, checkout mô phỏng và chat frontend kết nối được backend.

## 2. Việc chưa làm
- Chưa hoàn thiện bảo mật backend, chưa có Spring Security/JWT và phân quyền server-side.
- Chưa có báo cáo analytics/chatbot rõ ràng (lượt chat, chuyển đổi đơn hàng từ chat, doanh thu chat).
- Chưa hoàn chỉnh luồng đặt hàng/hóa đơn end-to-end (xử lý tạo hóa đơn, chi tiết hóa đơn, trạng thái đơn).
- Chưa kiểm thử responsive kỹ và chưa hoàn thiện phần báo cáo tài liệu cho đồ án.

## 3. Khuyến nghị nhanh
- Giữ nguyên phần đã có, ưu tiên thêm bảo mật backend cơ bản và tạo endpoint order/hóa đơn.
- Tập trung vào 2 phần: bảo mật + luồng đặt hàng, phần còn lại có thể để ở mức hiện tại.

