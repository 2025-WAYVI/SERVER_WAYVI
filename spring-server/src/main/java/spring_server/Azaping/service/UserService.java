package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_server.Azaping.entity.User;
import spring_server.Azaping.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 사용자 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * UUID로 사용자 등록 또는 조회
     */
    public Long registerOrGetUser(String uuid) {
        Optional<User> existingUser = userRepository.findByUuid(uuid);
        
        if (existingUser.isPresent()) {
            // 기존 사용자가 있으면 마지막 활동 시간 업데이트
            User user = existingUser.get();
            user.setLastActiveAt(LocalDateTime.now());
            userRepository.save(user);
            
            log.info("기존 사용자 조회 - uuid: {}, userId: {}", uuid, user.getUserId());
            return user.getUserId();
        }
        
        // 새 사용자 생성 (userId는 자동 생성)
        User newUser = new User();
        newUser.setUuid(uuid);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setLastActiveAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(newUser);
        log.info("새 사용자 등록 - uuid: {}, userId: {}", uuid, savedUser.getUserId());
        return savedUser.getUserId();
    }
    
    /**
     * userId로 사용자 존재 여부 확인
     */
    public boolean existsByUserId(Long userId) {
        return userRepository.existsByUserId(userId);
    }
    
    /**
     * 테스트 사용자 생성 (userId가 4가 되도록)
     */
    public void ensureTestUser() {
        if (!userRepository.existsByUserId(4L)) {
            log.info("테스트 사용자 (userId: 4) 생성 중...");
            
            // userId 4까지의 더미 사용자들 생성
            for (int i = 1; i <= 4; i++) {
                if (!userRepository.existsByUserId((long) i)) {
                    User testUser = new User();
                    testUser.setUuid("test-uuid-" + i);
                    testUser.setCreatedAt(LocalDateTime.now());
                    testUser.setLastActiveAt(LocalDateTime.now());
                    
                    User savedUser = userRepository.save(testUser);
                    log.info("테스트 사용자 생성 완료 - uuid: {}, userId: {}", 
                            testUser.getUuid(), savedUser.getUserId());
                }
            }
        }
    }
} 