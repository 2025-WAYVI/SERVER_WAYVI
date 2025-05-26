package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_server.Azaping.entity.User;
import spring_server.Azaping.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * UUID 기반 인증 서비스 구현
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    
    /**
     * UUID로 사용자 등록 또는 기존 사용자 조회
     * 기존 사용자가 있으면 해당 userId 반환, 없으면 새로 생성
     * 
     * @param uuid 클라이언트에서 생성한 UUID
     * @return 서버에서 발급한 userId
     */
    @Override
    @Transactional
    public String registerOrGetUser(String uuid) {
        log.debug("사용자 등록/조회 시작: UUID={}", uuid);
        
        // 기존 사용자 조회
        Optional<User> existingUser = userRepository.findByUuid(uuid);
        
        if (existingUser.isPresent()) {
            // 기존 사용자가 있으면 마지막 활동 시간 업데이트 후 userId 반환
            User user = existingUser.get();
            user.setLastActiveAt(LocalDateTime.now());
            userRepository.save(user);
            
            log.info("기존 사용자 조회: UUID={}, userId={}", uuid, user.getUserId());
            return user.getUserId();
        } else {
            // 새 사용자 등록
            String newUserId = generateUserId();
            User newUser = new User();
            newUser.setUuid(uuid);
            newUser.setUserId(newUserId);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastActiveAt(LocalDateTime.now());
            
            userRepository.save(newUser);
            
            log.info("새 사용자 등록: UUID={}, userId={}", uuid, newUserId);
            return newUserId;
        }
    }
    
    /**
     * 새로운 사용자 ID 생성 (user_001, user_002 형식)
     * 
     * @return 생성된 사용자 ID
     */
    private String generateUserId() {
        Integer nextNumber = userRepository.findNextUserNumber();
        return String.format("user_%03d", nextNumber);
    }
} 