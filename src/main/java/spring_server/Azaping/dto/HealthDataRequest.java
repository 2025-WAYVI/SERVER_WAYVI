package spring_server.Azaping.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthDataRequest {
    private String userId;
    private LocalDateTime timestamp;
    private DataType dataType;  // 데이터 유형 (REALTIME, DAILY, WEEKLY)
    
    // 실시간 데이터 (1시간마다)
    private Double heartRate;
    private Double bloodPressureSystolic;
    private Double bloodPressureDiastolic;
    private Double bloodOxygen;
    private Double bodyTemperature;
    
    // 일일 데이터 (하루 1회)
    private Integer steps;
    private Double sleepHours;
    private Double caloriesBurned;
    
    // 주간 데이터 (주 1회)
    private Double exerciseTime;
    private Double exerciseDistance;
    
    public enum DataType {
        REALTIME,   // 실시간 데이터
        DAILY,      // 일일 데이터
        WEEKLY      // 주간 데이터
    }
} 