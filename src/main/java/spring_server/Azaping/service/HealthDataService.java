package spring_server.Azaping.service;

import spring_server.Azaping.dto.HealthDataRequest;

/**
 * 건강 데이터 처리 서비스 인터페이스
 */
public interface HealthDataService {
    
    /**
     * 건강 데이터 저장
     * 
     * @param request 건강 데이터 요청
     * @throws IllegalArgumentException 유효하지 않은 사용자인 경우
     */
    void saveHealthData(HealthDataRequest request);
} 