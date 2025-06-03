package spring_server.Azaping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 실시간 건강 데이터 제출 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "실시간 건강 데이터 제출 응답")
public class RealtimeHealthDataResponse {
    
    /**
     * 응답 상태
     */
    @Schema(description = "응답 상태", example = "success")
    private String status;
    
    /**
     * 응답 메시지
     */
    @Schema(description = "응답 메시지", example = "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다.")
    private String message;
    
    /**
     * 이벤트 타입 (AI 분석 결과)
     * 가능한 값: "낙상/충돌", "심박 이상", "과로", "정상"
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "이벤트 타입", example = "정상", allowableValues = {"낙상/충돌", "심박 이상", "과로", "정상"})
    private String event;
    
    /**
     * 성공 응답 생성
     */
    public static RealtimeHealthDataResponse success(String event) {
        return new RealtimeHealthDataResponse(
            "success", 
            "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다.", 
            event
        );
    }
    
    /**
     * 에러 응답 생성
     */
    public static RealtimeHealthDataResponse error(String message) {
        return new RealtimeHealthDataResponse("error", message, null);
    }
} 