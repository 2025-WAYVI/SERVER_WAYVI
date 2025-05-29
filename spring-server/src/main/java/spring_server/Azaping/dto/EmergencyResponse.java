package spring_server.Azaping.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 구조요청 응답 DTO
 */
@Data
@Schema(description = "구조요청 응답")
public class EmergencyResponse {
    
    /**
     * 응답 상태
     */
    @Schema(description = "응답 상태", example = "success", allowableValues = {"success", "error"})
    private String status;
    
    /**
     * 응답 메시지
     */
    @Schema(description = "응답 메시지", example = "구조요청이 전송되었습니다.")
    private String message;
    
    /**
     * 구조요청 고유 ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "구조요청 고유 ID", example = "emergency_12345")
    private String requestId;
    
    /**
     * 성공 응답 생성
     */
    public static EmergencyResponse success(String requestId) {
        EmergencyResponse response = new EmergencyResponse();
        response.setStatus("success");
        response.setMessage("구조요청이 전송되었습니다.");
        response.setRequestId(requestId);
        return response;
    }
    
    /**
     * 실패 응답 생성
     */
    public static EmergencyResponse error(String message) {
        EmergencyResponse response = new EmergencyResponse();
        response.setStatus("error");
        response.setMessage(message);
        // requestId는 설정하지 않음 (null) - JSON에서 제외됨
        return response;
    }
} 