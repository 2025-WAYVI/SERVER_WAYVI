package spring_server.Azaping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 일일 건강 데이터 엔티티 (하루 1번 수집)
 */
@Entity
@Table(name = "daily_health_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyHealthData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 사용자 ID (User 엔티티의 userId와 연결)
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 데이터 제출 시각
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    /**
     * 걸음 수
     */
    @Column(nullable = false)
    private Integer stepCount;
    
    /**
     * 걸음 수 측정 시작 시각
     */
    @Column(nullable = false)
    private LocalDateTime stepCountStartDate;
    
    /**
     * 걸음 수 측정 종료 시각
     */
    @Column(nullable = false)
    private LocalDateTime stepCountEndDate;
    
    /**
     * 달리기 속도 (km/h) - JSON 배열로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String runningSpeed;
    
    /**
     * 달리기 측정 시작 시각
     */
    @Column(nullable = false)
    private LocalDateTime runningSpeedStartDate;
    
    /**
     * 달리기 측정 종료 시각
     */
    @Column(nullable = false)
    private LocalDateTime runningSpeedEndDate;
    
    /**
     * 기초 대사량 (kcal)
     */
    @Column(nullable = false)
    private Double basalEnergyBurned;
    
    /**
     * 활동 에너지 소모량 (kcal)
     */
    @Column(nullable = false)
    private Double activeEnergyBurned;
    
    /**
     * 활동 칼로리 측정 시작 시각
     */
    @Column(nullable = false)
    private LocalDateTime activeEnergyBurnedStartDate;
    
    /**
     * 활동 칼로리 측정 종료 시각
     */
    @Column(nullable = false)
    private LocalDateTime activeEnergyBurnedEndDate;
    
    /**
     * 신장 (cm) - 선택적
     */
    private Double height;
    
    /**
     * 체중 (kg) - 선택적
     */
    private Double bodyMass;
    
    /**
     * 산소포화도 (%) - JSON 배열로 저장 (8개 값)
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String oxygenSaturation;
    
    /**
     * 수축기 혈압 (mmHg) - 선택적
     */
    private Double bloodPressureSystolic;
    
    /**
     * 이완기 혈압 (mmHg) - 선택적
     */
    private Double bloodPressureDiastolic;
    
    /**
     * 호흡수 (회/분) - JSON 배열로 저장 (8개 값)
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String respiratoryRate;
    
    /**
     * 체온 (℃) - JSON 배열로 저장 (8개 값)
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String bodyTemperature;
    
    /**
     * 데이터 생성 시각
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 