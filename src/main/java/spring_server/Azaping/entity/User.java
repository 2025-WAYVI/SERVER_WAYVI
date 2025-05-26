package spring_server.Azaping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 사용자 정보를 저장하는 엔티티
 * UUID 기반 사용자 식별 시스템
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 클라이언트에서 생성한 UUID (고유 식별자)
     */
    @Column(unique = true, nullable = false)
    private String uuid;
    
    /**
     * 서버에서 발급하는 사용자 ID (user_001, user_002 형식)
     */
    @Column(unique = true, nullable = false)
    private String userId;
    
    /**
     * 사용자 등록 시각
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    /**
     * 마지막 활동 시각
     */
    private LocalDateTime lastActiveAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastActiveAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastActiveAt = LocalDateTime.now();
    }
} 