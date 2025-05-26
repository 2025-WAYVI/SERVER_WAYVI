package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 건강 데이터 제출 요청 DTO
 * API 명세서에 따른 실시간/일일 데이터 구조
 */
@Data
public class HealthDataRequest {
    
    /**
     * UUID 로그인으로 발급받은 사용자 ID
     */
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;
    
    /**
     * 제출 시각 (ISO 8601 형식)
     */
    @NotNull(message = "타임스탬프는 필수입니다.")
    private LocalDateTime timestamp;
    
    /**
     * 데이터 타입 (REALTIME 또는 DAILY)
     */
    @NotNull(message = "데이터 타입은 필수입니다.")
    private DataType dataType;
    
    // === 실시간 데이터 (5분에 1번) ===
    /**
     * 심박수 (bpm) - REALTIME 타입에서 필수
     */
    private Double heartRate;
    
    // === 일일 데이터 (하루 1번) ===
    /**
     * 걸음 수 - DAILY 타입에서 필수
     */
    private Integer stepCount;
    
    /**
     * 걸음 수 측정 시작 시각 (ISO 8601)
     */
    private LocalDateTime stepCountStartDate;
    
    /**
     * 걸음 수 측정 종료 시각 (ISO 8601)
     */
    private LocalDateTime stepCountEndDate;
    
    /**
     * 달리기 속도 (km/h)
     */
    private Double runningSpeed;
    
    /**
     * 달리기 측정 시작 시각 (ISO 8601)
     */
    private LocalDateTime runningSpeedStartDate;
    
    /**
     * 달리기 측정 종료 시각 (ISO 8601)
     */
    private LocalDateTime runningSpeedEndDate;
    
    /**
     * 기초 대사량 (kcal)
     */
    private Double basalEnergyBurned;
    
    /**
     * 활동으로 소모한 칼로리 (kcal)
     */
    private Double activeEnergyBurned;
    
    /**
     * 활동 칼로리 측정 시작 시각 (ISO 8601)
     */
    private LocalDateTime activeEnergyBurnedStartDate;
    
    /**
     * 활동 칼로리 측정 종료 시각 (ISO 8601)
     */
    private LocalDateTime activeEnergyBurnedEndDate;
    
    /**
     * 신장 (cm)
     */
    private Double height;
    
    /**
     * 체중 (kg)
     */
    private Double bodyMass;
    
    /**
     * 산소포화도 (%)
     */
    private Double oxygenSaturation;
    
    /**
     * 수축기 혈압 (mmHg)
     */
    private Integer bloodPressureSystolic;
    
    /**
     * 이완기 혈압 (mmHg)
     */
    private Integer bloodPressureDiastolic;
    
    /**
     * 호흡수 (회/분)
     */
    private Integer respiratoryRate;
    
    /**
     * 체온 (℃)
     */
    private Double bodyTemperature;
    
    /**
     * 데이터 타입 열거형
     */
    public enum DataType {
        REALTIME,   // 실시간 데이터 (5분마다)
        DAILY       // 일일 데이터 (하루 1번)
    }
} 