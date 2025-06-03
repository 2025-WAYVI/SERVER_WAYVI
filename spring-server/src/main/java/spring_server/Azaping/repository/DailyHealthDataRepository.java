package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.DailyHealthData;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 일일 건강 데이터 리포지토리
 */
@Repository
public interface DailyHealthDataRepository extends JpaRepository<DailyHealthData, Long> {
    
    /**
     * 특정 날짜의 일일 데이터 조회
     */
    @Query("SELECT d FROM DailyHealthData d WHERE d.userId = :userId AND DATE(d.timestamp) = DATE(:date)")
    Optional<DailyHealthData> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    /**
     * 사용자의 최근 일일 데이터 조회
     */
    @Query("SELECT d FROM DailyHealthData d WHERE d.userId = :userId ORDER BY d.timestamp DESC")
    List<DailyHealthData> findByUserIdOrderByTimestampDesc(@Param("userId") Long userId);
    
    /**
     * 특정 기간의 일일 데이터 조회
     */
    @Query("SELECT d FROM DailyHealthData d WHERE d.userId = :userId AND DATE(d.timestamp) BETWEEN DATE(:startDate) AND DATE(:endDate) ORDER BY d.timestamp ASC")
    List<DailyHealthData> findByUserIdAndDateBetween(
        @Param("userId") Long userId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * 사용자의 최근 N일 데이터 조회
     */
    @Query(value = "SELECT * FROM daily_health_data WHERE user_id = :userId ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<DailyHealthData> findRecentDataByUserId(@Param("userId") Long userId, @Param("limit") int limit);
    
    /**
     * 특정 날짜에 이미 데이터가 있는지 확인
     */
    @Query("SELECT COUNT(d) > 0 FROM DailyHealthData d WHERE d.userId = :userId AND DATE(d.timestamp) = DATE(:date)")
    boolean existsByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
} 