package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.EmergencyRequest;
import java.util.List;

/**
 * 구조요청 리포지토리
 */
@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Long> {
    
    /**
     * 사용자별 구조요청 이력 조회
     */
    List<EmergencyRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * requestId로 구조요청 조회
     */
    EmergencyRequest findByRequestId(String requestId);
} 