package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_server.Azaping.dto.HealthDataRequest;
import spring_server.Azaping.entity.HealthData;
import spring_server.Azaping.repository.HealthDataRepository;
import spring_server.Azaping.repository.UserRepository;

/**
 * 건강 데이터 처리 서비스 구현
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthDataServiceImpl implements HealthDataService {
    
    private final HealthDataRepository healthDataRepository;
    private final UserRepository userRepository;

    /**
     * 건강 데이터 저장
     * 사용자 유효성 검증 후 데이터베이스에 저장
     * 
     * @param request 건강 데이터 요청
     * @throws IllegalArgumentException 유효하지 않은 사용자인 경우
     */
    @Override
    @Transactional
    public void saveHealthData(HealthDataRequest request) {
        log.debug("건강 데이터 저장 시작: userId={}, dataType={}", request.getUserId(), request.getDataType());
        
        // 사용자 유효성 검증
        validateUser(request.getUserId());
        
        // 데이터 타입별 유효성 검증
        validateHealthDataByType(request);

        // HealthData 엔티티 생성 및 저장
        HealthData healthData = createHealthDataEntity(request);
        healthDataRepository.save(healthData);
        
        log.info("건강 데이터 저장 완료: userId={}, dataType={}, id={}", 
                request.getUserId(), request.getDataType(), healthData.getId());
    }
    
    /**
     * 사용자 유효성 검증
     * 
     * @param userId 사용자 ID
     * @throws IllegalArgumentException 유효하지 않은 사용자인 경우
     */
    private void validateUser(String userId) {
        if (!userRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID: " + userId);
    }
    }
    
    /**
     * 데이터 타입별 유효성 검증
     * 
     * @param request 건강 데이터 요청
     * @throws IllegalArgumentException 필수 필드가 누락된 경우
     */
    private void validateHealthDataByType(HealthDataRequest request) {
        if (request.getDataType() == HealthDataRequest.DataType.REALTIME) {
            // 실시간 데이터: 심박수 필수
            if (request.getHeartRate() == null) {
                throw new IllegalArgumentException("실시간 데이터에서 심박수는 필수입니다.");
            }
        } else if (request.getDataType() == HealthDataRequest.DataType.DAILY) {
            // 일일 데이터: 모든 필드 null 허용
            // 클라이언트에서 측정 가능한 데이터만 전송하도록 유연하게 처리
            log.debug("일일 데이터 수신: 모든 필드 null 허용");
        }
    }
    
    /**
     * HealthData 엔티티 생성
     * 
     * @param request 건강 데이터 요청
     * @return 생성된 HealthData 엔티티
     */
    private HealthData createHealthDataEntity(HealthDataRequest request) {
        HealthData healthData = new HealthData();
        
        // 공통 필드
        healthData.setUserId(request.getUserId());
        healthData.setTimestamp(request.getTimestamp());
        healthData.setDataType(HealthData.DataType.valueOf(request.getDataType().name()));
        
        // 실시간 데이터
        healthData.setHeartRate(request.getHeartRate());
        
        // 일일 데이터
        healthData.setStepCount(request.getStepCount());
        healthData.setStepCountStartDate(request.getStepCountStartDate());
        healthData.setStepCountEndDate(request.getStepCountEndDate());
        healthData.setRunningSpeed(request.getRunningSpeed());
        healthData.setRunningSpeedStartDate(request.getRunningSpeedStartDate());
        healthData.setRunningSpeedEndDate(request.getRunningSpeedEndDate());
        healthData.setBasalEnergyBurned(request.getBasalEnergyBurned());
        healthData.setActiveEnergyBurned(request.getActiveEnergyBurned());
        healthData.setActiveEnergyBurnedStartDate(request.getActiveEnergyBurnedStartDate());
        healthData.setActiveEnergyBurnedEndDate(request.getActiveEnergyBurnedEndDate());
        healthData.setHeight(request.getHeight());
        healthData.setBodyMass(request.getBodyMass());
        healthData.setOxygenSaturation(request.getOxygenSaturation());
        healthData.setBloodPressureSystolic(request.getBloodPressureSystolic());
        healthData.setBloodPressureDiastolic(request.getBloodPressureDiastolic());
        healthData.setRespiratoryRate(request.getRespiratoryRate());
        healthData.setBodyTemperature(request.getBodyTemperature());
        
        return healthData;
    }
} 