package spring_server.Azaping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_server.Azaping.dto.RealtimeHealthDataRequest;
import spring_server.Azaping.dto.DailyHealthDataRequest;
import spring_server.Azaping.entity.RealtimeHealthData;
import spring_server.Azaping.entity.DailyHealthData;
import spring_server.Azaping.repository.RealtimeHealthDataRepository;
import spring_server.Azaping.repository.DailyHealthDataRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 건강 데이터 서비스 (분리된 실시간/일일 데이터 처리)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthDataService {
    
    private final RealtimeHealthDataRepository realtimeHealthDataRepository;
    private final DailyHealthDataRepository dailyHealthDataRepository;
    private final AIService aiService;
    private final ObjectMapper objectMapper;
    
    /**
     * 실시간 건강 데이터 처리 (5분에 1번)
     */
    public String processRealtimeHealthData(Long userId, RealtimeHealthDataRequest request) {
        // 1. 데이터 저장
        RealtimeHealthData healthData = convertRealtimeToEntity(userId, request);
        
        // 2. 실시간 데이터 이상 탐지 수행
        List<RealtimeHealthData> recentData = realtimeHealthDataRepository.findTop5ByUserIdOrderByTimestampDesc(userId);
        
        String event = "정상";
        if (recentData.size() >= 4) { // 기존 4개 + 새로운 1개 = 5개
            // AI 분석 수행
            Map<String, Object> aiResult = performAnomalyDetection(userId, recentData);
            event = (String) aiResult.get("event");
            if (event == null) event = "정상";
        }
        
        // 3. 이벤트 정보와 함께 저장
        healthData.setEvent(event);
        realtimeHealthDataRepository.save(healthData);
        
        log.info("실시간 건강 데이터 저장 완료 - userId: {}, event: {}", userId, event);
        return event;
    }
    
    /**
     * 일일 건강 데이터 처리 (하루 1번)
     */
    public void processDailyHealthData(Long userId, DailyHealthDataRequest request) {
        // 1. 중복 체크 (같은 날짜에 이미 데이터가 있는지)
        boolean exists = dailyHealthDataRepository.existsByUserIdAndDate(userId, request.getTimestamp());
        if (exists) {
            log.warn("해당 날짜에 이미 일일 데이터가 존재합니다 - userId: {}, date: {}", 
                userId, request.getTimestamp().toLocalDate());
            return;
        }
        
        // 2. 데이터 저장
        DailyHealthData healthData = convertDailyToEntity(userId, request);
        dailyHealthDataRepository.save(healthData);
        
        // 3. AI 리포트 생성 (비동기로 처리 가능)
        generateHealthReport(userId, request);
        
        log.info("일일 건강 데이터 저장 완료 - userId: {}", userId);
    }
    
    /**
     * 건강 리포트 조회
     */
    public Map<String, Object> getHealthReport(Long userId, LocalDateTime date) {
        Optional<DailyHealthData> dailyDataOpt = dailyHealthDataRepository.findByUserIdAndDate(userId, date);
        
        if (dailyDataOpt.isEmpty()) {
            log.warn("해당 날짜의 일일 데이터가 없습니다 - userId: {}, date: {}", userId, date.toLocalDate());
            return null;
        }
        
        DailyHealthData data = dailyDataOpt.get();
        
        // 해당 날짜의 실시간 데이터에서 심박수 정보 가져오기
        List<RealtimeHealthData> realtimeData = realtimeHealthDataRepository.findByUserIdAndDate(userId, date);
        List<Double> heartRate = realtimeData.stream()
            .map(RealtimeHealthData::getHeartRate)
            .toList();
        
        // AI 서버에서 리포트 생성
        try {
            List<Double> runningSpeed = parseJsonToList(data.getRunningSpeed());
            List<Double> oxygenSaturation = parseJsonToList(data.getOxygenSaturation());
            List<Double> respiratoryRate = parseJsonToList(data.getRespiratoryRate());
            List<Double> bodyTemperature = parseJsonToList(data.getBodyTemperature());
            
            // 심박수 데이터가 없으면 기본값 사용
            if (heartRate.isEmpty()) {
                heartRate = List.of(80.0, 82.0, 79.0, 85.0, 84.0);
            }
            
            Map<String, Object> report = aiService.generateHealthReport(
                userId.toString(), date.toLocalDate().toString(),
                data.getStepCount(), runningSpeed,
                data.getBasalEnergyBurned(), data.getActiveEnergyBurned(),
                heartRate, oxygenSaturation, respiratoryRate, bodyTemperature
            );
            
            // 응답 형식에 맞게 변환
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("userId", userId);
            response.put("date", date.toLocalDate().toString());
            response.putAll(report);
            
            return response;
            
        } catch (Exception e) {
            log.error("건강 리포트 생성 실패 - userId: {}, error: {}", userId, e.getMessage());
            return null;
        }
    }
    
    /**
     * 사용자의 최근 실시간 데이터 조회
     */
    public List<RealtimeHealthData> getRecentRealtimeData(Long userId, int limit) {
        List<RealtimeHealthData> allData = realtimeHealthDataRepository.findTop5ByUserIdOrderByTimestampDesc(userId);
        return allData.stream().limit(limit).toList();
    }
    
    /**
     * 사용자의 최근 일일 데이터 조회
     */
    public List<DailyHealthData> getRecentDailyData(Long userId, int limit) {
        return dailyHealthDataRepository.findRecentDataByUserId(userId, limit);
    }
    
    /**
     * 이상 탐지 수행
     */
    private Map<String, Object> performAnomalyDetection(Long userId, List<RealtimeHealthData> recentData) {
        try {
            // 최근 5개 데이터에서 필요한 정보 추출
            List<List<Double>> accelData = new ArrayList<>();
            List<List<Double>> gyroData = new ArrayList<>();
            List<Double> heartRateData = new ArrayList<>();
            List<Double> runningSpeedData = new ArrayList<>();
            
            for (RealtimeHealthData data : recentData) {
                heartRateData.add(data.getHeartRate());
                
                if (data.getAccel() != null) {
                    accelData.add(parseJsonToList(data.getAccel()));
                }
                
                if (data.getGyro() != null) {
                    gyroData.add(parseJsonToList(data.getGyro()));
                }
                
                if (data.getRunningSpeed() != null) {
                    List<Double> speeds = parseJsonToList(data.getRunningSpeed());
                    if (!speeds.isEmpty()) {
                        runningSpeedData.add(speeds.get(0)); // 첫 번째 값만 사용
                    }
                }
            }
            
            // 최신 데이터의 stepCount와 activeEnergyBurned 사용
            RealtimeHealthData latestData = recentData.get(0);
            
            return aiService.analyzeAnomalies(
                userId.toString(), accelData, gyroData, heartRateData,
                latestData.getStepCount(), latestData.getActiveEnergyBurned(),
                runningSpeedData
            );
            
        } catch (Exception e) {
            log.error("이상 탐지 수행 실패 - userId: {}, error: {}", userId, e.getMessage());
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId.toString());
            result.put("event", "정상");
            return result;
        }
    }
    
    /**
     * 건강 리포트 생성
     */
    private void generateHealthReport(Long userId, DailyHealthDataRequest request) {
        // 비동기로 처리하거나 별도 스케줄러에서 처리 가능
        log.info("건강 리포트 생성 요청 - userId: {}", userId);
    }
    
    /**
     * 실시간 DTO를 Entity로 변환
     */
    private RealtimeHealthData convertRealtimeToEntity(Long userId, RealtimeHealthDataRequest request) {
        RealtimeHealthData entity = new RealtimeHealthData();
        entity.setUserId(userId);
        entity.setTimestamp(request.getTimestamp());
        
        // 실시간 데이터
        entity.setHeartRate(request.getHeartRate());
        entity.setStepCount(request.getStepCount());
        entity.setActiveEnergyBurned(request.getActiveEnergyBurned());
        
        if (request.getRunningSpeed() != null) {
            entity.setRunningSpeed(listToJson(request.getRunningSpeed()));
        }
        if (request.getAccel() != null) {
            entity.setAccel(listToJson(request.getAccel()));
        }
        if (request.getGyro() != null) {
            entity.setGyro(listToJson(request.getGyro()));
        }
        
        return entity;
    }
    
    /**
     * 일일 DTO를 Entity로 변환
     */
    private DailyHealthData convertDailyToEntity(Long userId, DailyHealthDataRequest request) {
        DailyHealthData entity = new DailyHealthData();
        entity.setUserId(userId);
        entity.setTimestamp(request.getTimestamp());
        
        // 일일 데이터
        entity.setStepCount(request.getStepCount());
        entity.setStepCountStartDate(request.getStepCountStartDate());
        entity.setStepCountEndDate(request.getStepCountEndDate());
        
        if (request.getRunningSpeed() != null) {
            entity.setRunningSpeed(listToJson(request.getRunningSpeed()));
        }
        entity.setRunningSpeedStartDate(request.getRunningSpeedStartDate());
        entity.setRunningSpeedEndDate(request.getRunningSpeedEndDate());
        
        entity.setBasalEnergyBurned(request.getBasalEnergyBurned());
        entity.setActiveEnergyBurned(request.getActiveEnergyBurned());
        entity.setActiveEnergyBurnedStartDate(request.getActiveEnergyBurnedStartDate());
        entity.setActiveEnergyBurnedEndDate(request.getActiveEnergyBurnedEndDate());
        
        entity.setHeight(request.getHeight());
        entity.setBodyMass(request.getBodyMass());
        
        if (request.getOxygenSaturation() != null) {
            entity.setOxygenSaturation(listToJson(request.getOxygenSaturation()));
        }
        entity.setBloodPressureSystolic(request.getBloodPressureSystolic());
        entity.setBloodPressureDiastolic(request.getBloodPressureDiastolic());
        
        if (request.getRespiratoryRate() != null) {
            entity.setRespiratoryRate(listToJson(request.getRespiratoryRate()));
        }
        if (request.getBodyTemperature() != null) {
            entity.setBodyTemperature(listToJson(request.getBodyTemperature()));
        }
        
        return entity;
    }
    
    /**
     * List를 JSON 문자열로 변환
     */
    private String listToJson(List<Double> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("List to JSON 변환 실패: {}", e.getMessage());
            return "[]";
        }
    }
    
    /**
     * JSON 문자열을 List로 변환
     */
    private List<Double> parseJsonToList(String json) {
        try {
            return objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class));
        } catch (Exception e) {
            log.error("JSON to List 변환 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
} 