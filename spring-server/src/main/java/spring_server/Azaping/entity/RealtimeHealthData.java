package spring_server.Azaping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 실시간 건강 데이터 엔티티 (5분마다 수집)
 */
@Entity
@Table(name = "realtime_health_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeHealthData {
    
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
     * 심박수 (bpm)
     */
    @Column(nullable = false)
    private Double heartRate;
    
    /**
     * 총합 걸음수
     */
    @Column(nullable = false)
    private Integer stepCount;
    
    /**
     * 활동 에너지 소모량 (kcal)
     */
    @Column(nullable = false)
    private Double activeEnergyBurned;
    
    /**
     * 달리기 속도 (km/h) - JSON 배열로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String runningSpeed;
    
    /**
     * 가속도계 데이터 (x, y, z) - JSON 배열로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String accel;
    
    /**
     * 자이로스코프 데이터 (x, y, z) - JSON 배열로 저장
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String gyro;
    
    /**
     * AI 분석 결과 이벤트
     */
    @Column(length = 50)
    private String event;
    
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