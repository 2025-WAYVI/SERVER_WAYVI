package spring_server.Azaping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 일일 건강 데이터 제출 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "일일 건강 데이터 제출 응답")
public class DailyHealthDataResponse {
    
    /**
     * 응답 상태
     */
    @Schema(description = "응답 상태", example = "success")
    private String status;
    
    /**
     * 응답 메시지
     */
    @Schema(description = "응답 메시지", example = "일일 건강 데이터가 성공적으로 수신되었습니다.")
    private String message;
    
    /**
     * 성공 응답 생성
     */
    public static DailyHealthDataResponse success() {
        return new DailyHealthDataResponse("success", "일일 건강 데이터가 성공적으로 수신되었습니다.");
    }
    
    /**
     * 에러 응답 생성
     */
    public static DailyHealthDataResponse error(String message) {
        return new DailyHealthDataResponse("error", message);
    }
} 