package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.User;
import java.util.Optional;

/**
 * 사용자 데이터 접근을 위한 Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * UUID로 사용자 조회
     * @param uuid 클라이언트에서 생성한 UUID
     * @return 사용자 정보
     */
    Optional<User> findByUuid(String uuid);
    
    /**
     * userId로 사용자 조회
     * @param userId 서버에서 발급한 사용자 ID
     * @return 사용자 정보
     */
    Optional<User> findByUserId(String userId);
    
    /**
     * 다음 사용자 ID 번호 조회 (user_001, user_002 형식을 위함)
     * @return 다음 사용자 번호
     */
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(u.userId, 6) AS int)), 0) + 1 FROM User u WHERE u.userId LIKE 'user_%'")
    Integer findNextUserNumber();
} 