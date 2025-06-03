package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_server.Azaping.dto.EmergencyRequest;
import spring_server.Azaping.dto.EmergencyResponse;
import spring_server.Azaping.repository.EmergencyRequestRepository;
import java.util.UUID;

/**
 * 구조요청 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyService {
    
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final EmailService emailService;
    
    /**
     * 구조요청 처리
     */
    public EmergencyResponse processEmergencyRequest(Long userId, EmergencyRequest request) {
        try {
            // 1. 고유 requestId 생성
            String requestId = "emergency_" + UUID.randomUUID().toString().substring(0, 8);
            
            // 2. 데이터베이스에 저장
            spring_server.Azaping.entity.EmergencyRequest entity = new spring_server.Azaping.entity.EmergencyRequest();
            entity.setRequestId(requestId);
            entity.setUserId(userId);
            entity.setEvent(request.getEvent());
            entity.setLatitude(request.getLatitude());
            entity.setLongitude(request.getLongitude());
            entity.setTimestamp(request.getTimestamp());
            
            emergencyRequestRepository.save(entity);
            
            // 3. 이메일 전송
            emailService.sendEmergencyEmail(
                userId,
                request.getEvent(),
                request.getLatitude(),
                request.getLongitude(),
                request.getTimestamp()
            );
            
            log.info("구조요청 처리 완료 - requestId: {}, userId: {}, event: {}", 
                    requestId, userId, request.getEvent());
            
            return EmergencyResponse.success(requestId);
            
        } catch (Exception e) {
            log.error("구조요청 처리 실패 - userId: {}, error: {}", userId, e.getMessage());
            return EmergencyResponse.error("구조요청 처리 중 오류가 발생했습니다.");
        }
    }
} 