package spring_server.Azaping.service;

/**
 * UUID 기반 인증 서비스 인터페이스
 */
public interface AuthService {
    
    /**
     * UUID로 사용자 등록 또는 기존 사용자 조회
     * 
     * @param uuid 클라이언트에서 생성한 UUID
     * @return 서버에서 발급한 userId
     */
    String registerOrGetUser(String uuid);
} 