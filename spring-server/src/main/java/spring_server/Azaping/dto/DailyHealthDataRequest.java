package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 일일 건강 데이터 제출 요청 DTO (하루 1번)
 */
@Data
@Schema(description = "일일 건강 데이터 제출 요청")
public class DailyHealthDataRequest {
    
    /**
     * 제출 시각 (ISO 8601 형식)
     */
    @NotNull(message = "타임스탬프는 필수입니다.")
    @Schema(description = "제출 시각 (ISO 8601 형식)", example = "2024-07-30T08:00:00Z", required = true)
    private LocalDateTime timestamp;
    
    /**
     * 걸음 수
     */
    @NotNull(message = "걸음수는 필수입니다.")
    @Schema(description = "걸음 수", example = "12050", required = true)
    private Integer stepCount;
    
    /**
     * 걸음 수 측정 시작 시각 (ISO 8601)
     */
    @NotNull(message = "걸음수 측정 시작 시각은 필수입니다.")
    @Schema(description = "걸음 수 측정 시작 시각 (ISO 8601)", example = "2024-07-29T00:00:00Z", required = true)
    private LocalDateTime stepCountStartDate;
    
    /**
     * 걸음 수 측정 종료 시각 (ISO 8601)
     */
    @NotNull(message = "걸음수 측정 종료 시각은 필수입니다.")
    @Schema(description = "걸음 수 측정 종료 시각 (ISO 8601)", example = "2024-07-29T23:59:59Z", required = true)
    private LocalDateTime stepCountEndDate;
    
    /**
     * 달리기 속도 (km/h) - 가변 길이 배열
     */
    @NotNull(message = "달리기 속도는 필수입니다.")
    @Schema(description = "달리기 속도 (km/h)", example = "[10.0, 9.0, 10.0, 11.0, 7.0]", required = true)
    private List<Double> runningSpeed;
    
    /**
     * 달리기 측정 시작 시각 (ISO 8601)
     */
    @NotNull(message = "달리기 측정 시작 시각은 필수입니다.")
    @Schema(description = "달리기 측정 시작 시각 (ISO 8601)", example = "2024-07-29T00:00:00Z", required = true)
    private LocalDateTime runningSpeedStartDate;
    
    /**
     * 달리기 측정 종료 시각 (ISO 8601)
     */
    @NotNull(message = "달리기 측정 종료 시각은 필수입니다.")
    @Schema(description = "달리기 측정 종료 시각 (ISO 8601)", example = "2024-07-29T23:59:59Z", required = true)
    private LocalDateTime runningSpeedEndDate;
    
    /**
     * 기초 대사량 (kcal)
     */
    @NotNull(message = "기초 대사량은 필수입니다.")
    @Schema(description = "기초 대사량 (kcal)", example = "1500.0", required = true)
    private Double basalEnergyBurned;
    
    /**
     * 활동 에너지 소모량 (kcal)
     */
    @NotNull(message = "활동 에너지 소모량은 필수입니다.")
    @Schema(description = "활동 에너지 소모량 (kcal)", example = "650.0", required = true)
    private Double activeEnergyBurned;
    
    /**
     * 활동 칼로리 측정 시작 시각 (ISO 8601)
     */
    @NotNull(message = "활동 칼로리 측정 시작 시각은 필수입니다.")
    @Schema(description = "활동 칼로리 측정 시작 시각 (ISO 8601)", example = "2024-07-29T00:00:00Z", required = true)
    private LocalDateTime activeEnergyBurnedStartDate;
    
    /**
     * 활동 칼로리 측정 종료 시각 (ISO 8601)
     */
    @NotNull(message = "활동 칼로리 측정 종료 시각은 필수입니다.")
    @Schema(description = "활동 칼로리 측정 종료 시각 (ISO 8601)", example = "2024-07-29T23:59:59Z", required = true)
    private LocalDateTime activeEnergyBurnedEndDate;
    
    /**
     * 신장 (cm) - 선택적
     */
    @Schema(description = "신장 (cm)", example = "170.0")
    private Double height;
    
    /**
     * 체중 (kg) - 선택적
     */
    @Schema(description = "체중 (kg)", example = "68.5")
    private Double bodyMass;
    
    /**
     * 산소포화도 (%) - 8개 값
     */
    @NotNull(message = "산소포화도는 필수입니다.")
    @Schema(description = "산소포화도 (%) - 8개 값", example = "[97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5]", required = true)
    private List<Double> oxygenSaturation;
    
    /**
     * 수축기 혈압 (mmHg) - 선택적
     */
    @Schema(description = "수축기 혈압 (mmHg)", example = "120.0")
    private Double bloodPressureSystolic;
    
    /**
     * 이완기 혈압 (mmHg) - 선택적
     */
    @Schema(description = "이완기 혈압 (mmHg)", example = "80.0")
    private Double bloodPressureDiastolic;
    
    /**
     * 호흡수 (회/분) - 8개 값
     */
    @NotNull(message = "호흡수는 필수입니다.")
    @Schema(description = "호흡수 (회/분) - 8개 값", example = "[18.0, 18.0, 18.0, 18.0, 19.0, 18.0, 18.0, 18.0]", required = true)
    private List<Double> respiratoryRate;
    
    /**
     * 체온 (℃) - 8개 값
     */
    @NotNull(message = "체온은 필수입니다.")
    @Schema(description = "체온 (℃) - 8개 값", example = "[36.5, 36.5, 36.6, 36.6, 36.7, 36.6, 36.6, 36.7]", required = true)
    private List<Double> bodyTemperature;
} 