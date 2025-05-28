package spring_server.Azaping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UUID 로그인 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "UUID 로그인 응답")
public class UuidLoginResponse {
    
    /**
     * 응답 상태
     */
    @Schema(description = "응답 상태", example = "success", allowableValues = {"success", "error"})
    private String status;
    
    /**
     * 발급된 사용자 ID
     */
    @Schema(description = "발급된 사용자 ID", example = "1")
    private Long userId;
    
    /**
     * 응답 메시지 (오류 시)
     */
    @Schema(description = "응답 메시지", example = "로그인 성공")
    private String message;
    
    /**
     * 성공 응답 생성
     */
    public static UuidLoginResponse success(Long userId) {
        return new UuidLoginResponse("success", userId, null);
    }
    
    /**
     * 실패 응답 생성
     */
    public static UuidLoginResponse error(String message) {
        return new UuidLoginResponse("error", null, message);
    }
} 