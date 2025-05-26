package spring_server.Azaping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * UUID 로그인 응답 DTO
 */
@Data
@AllArgsConstructor
public class UuidLoginResponse {
    
    /**
     * 응답 상태 ("success" 또는 "error")
     */
    private String status;
    
    /**
     * 서버에서 발급한 사용자 ID (user_001 형식)
     */
    private String userId;
    
    /**
     * 성공 응답 생성
     * @param userId 발급된 사용자 ID
     * @return 성공 응답
     */
    public static UuidLoginResponse success(String userId) {
        return new UuidLoginResponse("success", userId);
    }
} 