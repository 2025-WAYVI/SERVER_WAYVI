package spring_server.Azaping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * 건강 데이터 제출 응답 DTO
 */
@Data
@AllArgsConstructor
public class HealthDataResponse {
    
    /**
     * 응답 상태 ("success" 또는 "error")
     */
    private String status;
    
    /**
     * 응답 메시지
     */
    private String message;
    
    /**
     * 성공 응답 생성
     * @return 성공 응답
     */
    public static HealthDataResponse success() {
        return new HealthDataResponse("success", "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다.");
    }
    
    /**
     * 에러 응답 생성
     * @param message 에러 메시지
     * @return 에러 응답
     */
    public static HealthDataResponse error(String message) {
        return new HealthDataResponse("error", message);
    }
} 