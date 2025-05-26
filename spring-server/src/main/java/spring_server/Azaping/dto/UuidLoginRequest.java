package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * UUID 로그인 요청 DTO
 */
@Data
public class UuidLoginRequest {
    
    /**
     * 클라이언트에서 생성한 UUID (v4 형식)
     * 예시: "550e8400-e29b-41d4-a716-446655440000"
     */
    @NotBlank(message = "UUID는 필수입니다.")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
             message = "올바른 UUID 형식이 아닙니다.")
    private String uuid;
} 