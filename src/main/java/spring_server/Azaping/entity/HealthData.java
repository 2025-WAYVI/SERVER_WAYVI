package spring_server.Azaping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 건강 데이터를 저장하는 엔티티
 * 실시간 데이터(REALTIME)와 일일 데이터(DAILY) 모두 저장
 */
@Entity
@Table(name = "health_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 사용자 ID (User 엔티티의 userId와 연결)
     */
    @Column(nullable = false)
    private String userId;
    
    /**
     * 데이터 제출 시각
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 데이터 타입 (REALTIME 또는 DAILY)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType dataType;
    
    // === 실시간 데이터 (5분마다) ===
    /**
     * 심박수 (bpm)
     */
    private Double heartRate;
    
    // === 일일 데이터 (하루 1번) ===
    /**
     * 걸음 수
     */
    private Integer stepCount;
    
    /**
     * 걸음 수 측정 시작 시각
     */
    private LocalDateTime stepCountStartDate;
    
    /**
     * 걸음 수 측정 종료 시각
     */
    private LocalDateTime stepCountEndDate;
    
    /**
     * 달리기 속도 (km/h)
     */
    private Double runningSpeed;
    
    /**
     * 달리기 측정 시작 시각
     */
    private LocalDateTime runningSpeedStartDate;
    
    /**
     * 달리기 측정 종료 시각
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
     * 활동 칼로리 측정 시작 시각
     */
    private LocalDateTime activeEnergyBurnedStartDate;
    
    /**
     * 활동 칼로리 측정 종료 시각
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
     * 데이터 생성 시각
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    /**
     * 데이터 타입 열거형
     */
    public enum DataType {
        REALTIME,   // 실시간 데이터 (5분마다)
        DAILY       // 일일 데이터 (하루 1번)
    }
} 