package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 구조요청 DTO
 * 프론트엔드에서 미응답 시 구조요청을 위한 데이터 구조
 */
@Data
@Schema(description = "구조요청 데이터")
public class EmergencyRequest {
    
    /**
     * 실시간 이상징후
     * 가능한 값: "낙상/충돌", "심박 이상", "움직임 없음"
     */
    @NotBlank(message = "이벤트 타입은 필수입니다.")
    @Schema(description = "실시간 이상징후", example = "낙상/충돌", allowableValues = {"낙상/충돌", "심박 이상", "움직임 없음"}, required = true)
    private String event;
    
    /**
     * 사용자 위치 - 위도
     */
    @NotNull(message = "위도는 필수입니다.")
    @Schema(description = "사용자 위치 - 위도", example = "37.5665", required = true)
    private Double latitude;
    
    /**
     * 사용자 위치 - 경도
     */
    @NotNull(message = "경도는 필수입니다.")
    @Schema(description = "사용자 위치 - 경도", example = "126.9780", required = true)
    private Double longitude;
    
    /**
     * 구조요청 시각 (ISO 8601 형식)
     */
    @NotNull(message = "타임스탬프는 필수입니다.")
    @Schema(description = "구조요청 시각 (ISO 8601 형식)", example = "2024-07-30T10:05:00Z", required = true)
    private LocalDateTime timestamp;
} 