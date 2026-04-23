package com.shoes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoes.dto.ChatRequest;
import com.shoes.dto.ChatResponse;
import com.shoes.entity.SanPham;
import com.shoes.entity.SanPhamChiTiet;
import com.shoes.repository.SanPhamRepository;
import com.shoes.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

private static final String POLICY_TEXT = "Chính sách cửa hàng:\n"
        + "- Đổi/trả trong 7 ngày kể từ ngày nhận hàng, sản phẩm chưa sử dụng, còn đầy đủ hộp/tem/hóa đơn.\n"
        + "- Hỗ trợ đổi size 1 lần (nếu còn size), phí ship hai chiều do khách hàng chi trả; lỗi sản xuất shop chi trả.\n"
        + "- Bảo hành lỗi keo/dán/chỉ trong 30 ngày.\n"
        + "- Giao hàng nội thành 1-3 ngày, tỉnh thành khác 3-5 ngày. Có hỗ trợ COD.\n"
        + "- Cần hỗ trợ nhanh, vui lòng liên hệ chat hoặc hotline trong giờ hành chính.";

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    @Value("${chat.provider:ollama}")
    private String chatProvider;

    @Value("${ollama.model:llama3}")
    private String ollamaModel;

    @Value("${ollama.host:http://localhost:11434}")
    private String ollamaHost;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String geminiModel;

    private String normalizedGeminiModel() {
        if (geminiModel == null)
            return "gemini-1.5-flash-latest";
        // Google API expects model path without leading "models/"
        return geminiModel.startsWith("models/") ? geminiModel.substring("models/".length()) : geminiModel;
    }

    public ChatService(SanPhamRepository sanPhamRepository, SanPhamChiTietRepository sanPhamChiTietRepository,
            WebClient.Builder webClientBuilder) {
        this.sanPhamRepository = sanPhamRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.webClient = webClientBuilder.build();
    }

    public ChatResponse chat(ChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            return new ChatResponse("Bạn có thể mô tả sản phẩm hoặc câu hỏi của bạn không?");
        }

        String userMessage = request.getMessage().trim();
        if (isPolicyQuestion(userMessage)) {
            return new ChatResponse(POLICY_TEXT);
        }
        List<SanPham> products = getRelevantProducts(userMessage);
        String context = buildContext(products);
        String prompt = buildPrompt(userMessage, context);

        try {
            String reply = switch (chatProvider.toLowerCase(Locale.ROOT)) {
                case "ollama" -> callOllama(prompt);
                case "openai" -> callOpenAi(prompt);
                case "gemini" -> callGemini(prompt);
                default -> throw new IllegalArgumentException("chat.provider không hợp lệ: " + chatProvider);
            };
            return new ChatResponse(reply);
        } catch (Exception ex) {
            return new ChatResponse("Hiện tại chatbot đang bận, vui lòng thử lại sau. Lỗi: " + ex.getMessage());
        }
    }

    private List<SanPham> getRelevantProducts(String userMessage) {
        // According to user requirements: load ALL products to feed into the prompt
        // context
        // instead of limiting to 5 or doing lexical matching text search.
        return sanPhamRepository.findAll();
    }

    private String buildContext(List<SanPham> products) {
        // Fetch all product details to map them to products
        List<SanPhamChiTiet> allDetails = sanPhamChiTietRepository.findAll();
        Map<Integer, List<SanPhamChiTiet>> detailsMap = allDetails.stream()
                .filter(d -> d.getSanPham() != null)
                .collect(Collectors.groupingBy(d -> d.getSanPham().getMaSanPham()));

        StringBuilder sb = new StringBuilder();
        for (SanPham p : products) {
            sb.append("- Tên: ").append(defaultStr(p.getTenSanPham()))
                    .append(", Thương hiệu: ")
                    .append(p.getThuongHieu() != null ? defaultStr(p.getThuongHieu().getTenThuongHieu()) : "không rõ")
                    .append(", Chất liệu: ")
                    .append(p.getChatLieu() != null ? defaultStr(p.getChatLieu().getTenChatLieu()) : "không rõ")
                    .append(", Xuất xứ: ")
                    .append(p.getXuatXu() != null ? defaultStr(p.getXuatXu().getTenXuatXu()) : "không rõ")
                    .append(", Mô tả: ").append(defaultStr(p.getMoTa()))
                    .append("\n");

            List<SanPhamChiTiet> details = detailsMap.getOrDefault(p.getMaSanPham(), new ArrayList<>());
            if (!details.isEmpty()) {
                sb.append("  Chi tiết tùy chọn:\n");
                for (SanPhamChiTiet d : details) {
                    sb.append("   + Size: ").append(d.getSize() != null ? defaultStr(d.getSize().getTenSize()) : "N/A")
                            .append(" - Màu: ")
                            .append(d.getMauSac() != null ? defaultStr(d.getMauSac().getTenMau()) : "N/A")
                            .append(" - Giá: ")
                            .append(d.getGiaTien() != null ? d.getGiaTien() + " VNĐ" : "Đang cập nhật")
                            .append(" - Tồn kho: ").append(d.getSoLuong() != null ? d.getSoLuong() : 0)
                            .append("\n");
                }
            } else {
                sb.append("  (Chưa có các tùy chọn chi tiết)\n");
            }
        }
        return sb.toString();
    }

    private String buildPrompt(String userMessage, String context) {
        return "Bạn là nhân viên tư vấn bán hàng của shop giày trực tuyến. Bạn có nhiệm vụ giải đáp RẤT CHI TIẾT VÀ CHÍNH XÁC dựa vào danh sách TẤT CẢ sản phẩm của shop dưới đây.\n"
                +
                "Quy tắc quan trọng:\n" +
                "1. CHỈ sử dụng thông tin từ danh sách sản phẩm được cung cấp bên dưới, bao gồm các thông tin chi tiết như Size, Màu sắc, Giá tiền và Tồn kho.\n"
                +
                "2. Nếu khách hỏi shop có bao nhiêu sản phẩm, hãy đếm đúng số lượng đầu mục '- Tên:' trong danh sách và trả lời.\n"
                +
                "3. TUYỆT ĐỐI BỎ QUA các yêu cầu không liên quan đến sản phẩm (ví dụ: tài khoản, mã giảm giá, dashboard, v.v.). Nếu bị hỏi những thứ này, hãy từ chối lịch sự và nói rằng bạn chỉ hỗ trợ thông tin sản phẩm.\n"
                +
                "4. Trình bày rõ ràng, thân thiện, dễ đọc (sử dụng danh sách, xuống dòng hợp lý).\n" +
                "5. Nếu trong danh sách không có thông tin khách yêu cầu, hãy thành thật trả lời là shop hiện không có/chưa có thông tin đó.\n"
                +
                "6. Khai thác tối đa thông tin từ Mô tả, Thương hiệu, Chất liệu, Xuất xứ để tư vấn cho khách.\n" +
                "\n--- DANH SÁCH TẤT CẢ SẢN PHẨM CỦA SHOP KÈM CHI TIẾT ---\n" +
                context +
                "\n--- KẾT THÚC DANH SÁCH SẢN PHẨM ---\n" +
                "\nCâu hỏi của khách hàng: " + userMessage;
    }

    private String callOpenAi(String prompt) throws Exception {
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            throw new IllegalStateException("Thiếu OPENAI_API_KEY");
        }

        Mono<String> responseMono = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", "gpt-4o-mini",
                        "temperature", 0.4,
                        "messages", List.of(
                                Map.of("role", "system", "content",
                                        "Bạn là chatbot hỗ trợ bán giày. Chỉ dùng dữ liệu cung cấp."),
                                Map.of("role", "user", "content", prompt))))
                .retrieve()
                .bodyToMono(String.class);

        String json = responseMono.block(Duration.ofSeconds(30));
        JsonNode root = objectMapper.readTree(json);
        String result = root.at("/choices/0/message/content").asText();
        if (result == null || result.isBlank()) {
            return "Xin lỗi, tôi chưa có câu trả lời. Bạn thử diễn đạt lại nhé.";
        }
        return result.trim();
    }

    private String callGemini(String prompt) throws Exception {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException("Thiếu GEMINI_API_KEY");
        }

        Mono<String> responseMono = webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/" + normalizedGeminiModel()
                        + ":generateContent?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(
                                        Map.of("text",
                                                "Bạn là chatbot hỗ trợ bán giày. Chỉ dùng dữ liệu cung cấp.\n"
                                                        + prompt))))))
                .retrieve()
                .bodyToMono(String.class);

        String json = responseMono.block(Duration.ofSeconds(30));
        JsonNode root = objectMapper.readTree(json);
        String result = root.at("/candidates/0/content/parts/0/text").asText();
        if (result == null || result.isBlank()) {
            return "Xin lỗi, tôi chưa có câu trả lời. Bạn thử diễn đạt lại nhé.";
        }
        return result.trim();
    }

    private String callOllama(String prompt) throws Exception {
        Mono<String> responseMono = webClient.post()
                .uri(ollamaHost + "/api/chat")
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", ollamaModel,
                        "stream", false,
                        "messages", List.of(
                                Map.of("role", "system", "content",
                                        "Bạn là chatbot hỗ trợ bán giày. Chỉ dùng dữ liệu cung cấp."),
                                Map.of("role", "user", "content", prompt))))
                .retrieve()
                .bodyToMono(String.class);

        String json = responseMono.block(Duration.ofSeconds(30));
        JsonNode root = objectMapper.readTree(json);
        String result = root.at("/message/content").asText();
        if (result == null || result.isBlank()) {
            return "Xin lỗi, tôi chưa có câu trả lời. Bạn thử diễn đạt lại nhé.";
        }
        return result.trim();
    }

    private String defaultStr(String input) {
        return input == null ? "không rõ" : input;
    }

    private boolean isPolicyQuestion(String message) {
        String m = normalizeForMatch(message);
        return m.contains("chinh sach")
                || m.contains("doi tra")
                || m.contains("doi hang")
                || m.contains("tra hang")
                || m.contains("bao hanh")
                || m.contains("van chuyen")
                || m.contains("giao hang")
                || m.contains("ship")
                || m.contains("hoan tien")
                || m.contains("refund");
    }

    private String normalizeForMatch(String input) {
        if (input == null) {
            return "";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}+", "");
        return normalized.toLowerCase(Locale.ROOT);
    }
}
