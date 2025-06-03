package spring_server.Azaping.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UUID 로그인 요청 DTO
 */
@Data
@Schema(description = "UUID 로그인 요청")
public class UuidLoginRequest {
    
    /**
     * 클라이언트 디바이스 UUID
     */
    @NotBlank(message = "UUID는 필수입니다.")
    @Schema(description = "클라이언트 디바이스에서 생성한 UUID", 
            example = "550e8400-e29b-41d4-a716-446655440000", 
            required = true)
    private String uuid;
} 