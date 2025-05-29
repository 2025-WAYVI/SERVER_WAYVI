package spring_server.Azaping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 건강 리포트 엔티티
 */
@Entity
@Table(name = "health_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 사용자 ID
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 리포트 날짜
     */
    @Column(nullable = false)
    private LocalDate date;
    
    /**
     * 건강 상태 분류 ("건강유지형", "저활동형", "과로주의형", "위험신호형")
     */
    @Column(nullable = false, length = 20)
    private String summary;
    
    /**
     * 하루 총 걸음수
     */
    @Column(nullable = false)
    private Integer stepCount;
    
    /**
     * 평소 대비 걸음수 변화율 (%)
     */
    @Column(nullable = false)
    private Integer stepCountChange;
    
    /**
     * 평균 달리기 속도 (km/h)
     */
    @Column(nullable = false)
    private Double averageRunningSpeed;
    
    /**
     * 평소 대비 달리기 속도 변화 (km/h)
     */
    @Column(nullable = false)
    private Double runningSpeedChange;
    
    /**
     * 평균 심박수 (bpm)
     */
    @Column(nullable = false)
    private Integer averageHeartRate;
    
    /**
     * 평소 대비 심박수 변화율 (%)
     */
    @Column(nullable = false)
    private Integer heartRateChange;
    
    /**
     * 평균 산소포화도 (%)
     */
    @Column(nullable = false)
    private Integer averageOxygenSaturation;
    
    /**
     * 평균 호흡수 (회/분)
     */
    @Column(nullable = false)
    private Integer averageRespiratoryRate;
    
    /**
     * 평균 체온 (℃)
     */
    @Column(nullable = false)
    private Double averageBodyTemperature;
    
    /**
     * 활동 에너지 소모량 (kcal)
     */
    @Column(nullable = false)
    private Integer activeEnergyBurned;
    
    /**
     * 평소 대비 활동 에너지 변화율 (%)
     */
    @Column(nullable = false)
    private Integer activeEnergyChange;
    
    /**
     * 건강 주의사항 목록 - JSON 배열로 저장
     * 가능한 값: "부정맥", "빈맥", "서맥", "호흡기 질환", "저산소증", "저체온", "발열", "저혈압", "고혈압"
     */
    @Column(columnDefinition = "TEXT")
    private String warning;
    
    /**
     * 리포트 생성 시각
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 