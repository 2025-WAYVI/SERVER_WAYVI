package spring_server.Azaping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.dto.UuidLoginRequest;
import spring_server.Azaping.dto.UuidLoginResponse;
import spring_server.Azaping.service.AuthService;

/**
 * 인증 관련 API 컨트롤러
 * UUID 기반 사용자 등록 및 userId 발급
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "UUID 기반 인증 API")
public class AuthController {

    private final AuthService authService;

    /**
     * UUID 로그인 - 사용자 디바이스 UUID를 등록하고 userId 발급
     * 
     * @param request UUID 로그인 요청 (클라이언트 UUID 포함)
     * @return 발급된 userId를 포함한 응답
     */
    @Operation(summary = "UUID 로그인", 
               description = "클라이언트 디바이스에서 생성한 UUID를 서버에 등록하고 userId를 발급받습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공 - userId 발급"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 - UUID 형식 오류"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/uuid-login")
    public ResponseEntity<UuidLoginResponse> uuidLogin(@Validated @RequestBody UuidLoginRequest request) {
        log.info("UUID 로그인 요청: {}", request.getUuid());
        
        try {
            // UUID로 사용자 등록 또는 기존 사용자 조회
            String userId = authService.registerOrGetUser(request.getUuid());
            
            log.info("UUID 로그인 성공: UUID={}, userId={}", request.getUuid(), userId);
            return ResponseEntity.ok(UuidLoginResponse.success(userId));
            
        } catch (Exception e) {
            log.error("UUID 로그인 실패: UUID={}, error={}", request.getUuid(), e.getMessage());
            throw e;
        }
    }
} 