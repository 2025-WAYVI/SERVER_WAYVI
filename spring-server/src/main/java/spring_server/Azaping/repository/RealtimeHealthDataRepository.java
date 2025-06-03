package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.RealtimeHealthData;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 실시간 건강 데이터 리포지토리
 */
@Repository
public interface RealtimeHealthDataRepository extends JpaRepository<RealtimeHealthData, Long> {
    
    /**
     * 사용자의 최근 실시간 데이터 5개 조회
     */
    @Query("SELECT r FROM RealtimeHealthData r WHERE r.userId = :userId ORDER BY r.timestamp DESC")
    List<RealtimeHealthData> findTop5ByUserIdOrderByTimestampDesc(@Param("userId") Long userId);
    
    /**
     * 특정 날짜의 실시간 데이터 조회
     */
    @Query("SELECT r FROM RealtimeHealthData r WHERE r.userId = :userId AND DATE(r.timestamp) = DATE(:date) ORDER BY r.timestamp ASC")
    List<RealtimeHealthData> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    /**
     * 특정 기간의 실시간 데이터 조회
     */
    @Query("SELECT r FROM RealtimeHealthData r WHERE r.userId = :userId AND r.timestamp BETWEEN :startDate AND :endDate ORDER BY r.timestamp ASC")
    List<RealtimeHealthData> findByUserIdAndTimestampBetween(
        @Param("userId") Long userId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * 특정 이벤트 타입의 데이터 조회
     */
    @Query("SELECT r FROM RealtimeHealthData r WHERE r.userId = :userId AND r.event = :event ORDER BY r.timestamp DESC")
    List<RealtimeHealthData> findByUserIdAndEvent(@Param("userId") Long userId, @Param("event") String event);
} 