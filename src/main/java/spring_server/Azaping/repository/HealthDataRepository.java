package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.HealthData;
import java.util.List;

/**
 * 건강 데이터 접근을 위한 Repository
 */
@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
    
    /**
     * 특정 사용자의 건강 데이터 조회
     * @param userId 사용자 ID
     * @return 건강 데이터 목록
     */
    List<HealthData> findByUserIdOrderByTimestampDesc(String userId);
    
    /**
     * 특정 사용자의 특정 타입 건강 데이터 조회
     * @param userId 사용자 ID
     * @param dataType 데이터 타입 (REALTIME 또는 DAILY)
     * @return 건강 데이터 목록
     */
    List<HealthData> findByUserIdAndDataTypeOrderByTimestampDesc(String userId, HealthData.DataType dataType);
} 