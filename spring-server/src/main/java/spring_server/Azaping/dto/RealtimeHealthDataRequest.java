package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 실시간 건강 데이터 제출 요청 DTO (5분에 1번)
 */
@Data
@Schema(description = "실시간 건강 데이터 제출 요청")
public class RealtimeHealthDataRequest {
    
    /**
     * 제출 시각 (ISO 8601 형식)
     */
    @NotNull(message = "타임스탬프는 필수입니다.")
    @Schema(description = "제출 시각 (ISO 8601 형식)", example = "2024-07-30T10:05:00Z", required = true)
    private LocalDateTime timestamp;
    
    /**
     * 심박수 (bpm)
     */
    @NotNull(message = "심박수는 필수입니다.")
    @Schema(description = "심박수 (bpm)", example = "80.0", required = true)
    private Double heartRate;
    
    /**
     * 총합 걸음수
     */
    @NotNull(message = "걸음수는 필수입니다.")
    @Schema(description = "총합 걸음수", example = "7200", required = true)
    private Integer stepCount;
    
    /**
     * 활동 에너지 소모량 (kcal)
     */
    @NotNull(message = "활동 에너지 소모량은 필수입니다.")
    @Schema(description = "활동 에너지 소모량 (kcal)", example = "500.0", required = true)
    private Double activeEnergyBurned;
    
    /**
     * 달리기 속도 (km/h) - 가변 길이 배열
     */
    @NotNull(message = "달리기 속도는 필수입니다.")
    @Schema(description = "달리기 속도 (km/h)", example = "[15.0]", required = true)
    private List<Double> runningSpeed;
    
    /**
     * 가속도계 데이터 (x, y, z)
     */
    @NotNull(message = "가속도계 데이터는 필수입니다.")
    @Schema(description = "가속도계 데이터 (x, y, z)", example = "[0.12, 0.02, 0.98]", required = true)
    private List<Double> accel;
    
    /**
     * 자이로스코프 데이터 (x, y, z)
     */
    @NotNull(message = "자이로스코프 데이터는 필수입니다.")
    @Schema(description = "자이로스코프 데이터 (x, y, z)", example = "[0.02, 0.01, 0.03]", required = true)
    private List<Double> gyro;
} 