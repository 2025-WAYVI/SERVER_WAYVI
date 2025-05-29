package spring_server.Azaping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * AI 서버 연동 서비스 (Mock 구현)
 */
@Service
@Slf4j
public class AIService {
    
    /**
     * 이상 탐지 분석 (Mock)
     */
    public Map<String, Object> analyzeAnomalies(String userId, List<List<Double>> accel, 
                                               List<List<Double>> gyro, List<Double> heartRate,
                                               Integer stepCount, Double activeEnergyBurned, 
                                               List<Double> runningSpeed) {
        
        log.info("AI 이상 탐지 분석 요청 - userId: {}", userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        
        // Mock 로직: 심박수가 100 이상이면 이상 탐지
        boolean hasAbnormalHeartRate = heartRate.stream().anyMatch(hr -> hr > 100);
        
        // Mock 로직: 가속도가 급격히 변하면 낙상 감지
        boolean hasFallDetection = accel.stream()
            .anyMatch(acc -> Math.abs(acc.get(0)) > 2.0 || Math.abs(acc.get(1)) > 2.0 || Math.abs(acc.get(2)) > 2.0);
        
        // Mock 로직: 활동량이 과도하면 과로 감지
        boolean hasOverwork = activeEnergyBurned > 800;
        
        if (hasFallDetection) {
            result.put("event", "낙상/충돌");
        } else if (hasAbnormalHeartRate) {
            result.put("event", "심박 이상");
        } else if (hasOverwork) {
            result.put("event", "과로");
        } else {
            result.put("event", "정상");
        }
        
        log.info("AI 분석 결과: {}", result.get("event"));
        return result;
    }
    
    /**
     * 건강 리포트 생성 (Mock)
     */
    public Map<String, Object> generateHealthReport(String userId, String date, 
                                                   Integer stepCount, List<Double> runningSpeed,
                                                   Double basalEnergyBurned, Double activeEnergyBurned,
                                                   List<Double> heartRate, List<Double> oxygenSaturation,
                                                   List<Double> respiratoryRate, List<Double> bodyTemperature) {
        
        log.info("건강 리포트 생성 요청 - userId: {}, date: {}", userId, date);
        
        Map<String, Object> report = new HashMap<>();
        report.put("userId", userId);
        
        // Mock 건강 상태 분류
        String summary;
        if (stepCount > 8000 && activeEnergyBurned > 400) {
            summary = "건강유지형";
        } else if (stepCount < 3000) {
            summary = "저활동형";
        } else if (activeEnergyBurned > 700) {
            summary = "과로주의형";
        } else {
            summary = "위험신호형";
        }
        
        report.put("summary", summary);
        report.put("stepCount", stepCount);
        report.put("stepCountChange", (int)(Math.random() * 40) - 20); // -20 ~ +20
        
        double avgRunningSpeed = runningSpeed.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        report.put("averageRunningSpeed", Math.round(avgRunningSpeed * 10.0) / 10.0);
        report.put("runningSpeedChange", Math.round((Math.random() * 2 - 1) * 10.0) / 10.0); // -1.0 ~ +1.0
        
        double avgHeartRate = heartRate.stream().mapToDouble(Double::doubleValue).average().orElse(80.0);
        report.put("averageHeartRate", (int)avgHeartRate);
        report.put("heartRateChange", (int)(Math.random() * 30) - 15); // -15 ~ +15
        
        double avgOxygenSaturation = oxygenSaturation.stream().mapToDouble(Double::doubleValue).average().orElse(97.0);
        report.put("averageOxygenSaturation", (int)avgOxygenSaturation);
        
        double avgRespiratoryRate = respiratoryRate.stream().mapToDouble(Double::doubleValue).average().orElse(16.0);
        report.put("averageRespiratoryRate", (int)avgRespiratoryRate);
        
        double avgBodyTemperature = bodyTemperature.stream().mapToDouble(Double::doubleValue).average().orElse(36.5);
        report.put("averageBodyTemperature", Math.round(avgBodyTemperature * 10.0) / 10.0);
        
        report.put("activeEnergyBurned", activeEnergyBurned.intValue());
        report.put("activeEnergyChange", (int)(Math.random() * 40) - 20); // -20 ~ +20
        
        // Mock 경고 생성
        List<String> warnings = List.of();
        if (avgHeartRate > 90) {
            warnings = List.of("빈맥");
        } else if (avgBodyTemperature < 36.0) {
            warnings = List.of("저체온");
        }
        report.put("warning", warnings);
        
        log.info("건강 리포트 생성 완료 - summary: {}", summary);
        return report;
    }
} 