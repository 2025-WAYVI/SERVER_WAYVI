package spring_server.Azaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_server.Azaping.entity.User;
import java.util.Optional;

/**
 * 사용자 리포지토리
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
    Optional<User> findByUserId(Long userId);
    
    /**
     * userId 존재 여부 확인
     */
    boolean existsByUserId(Long userId);
} 