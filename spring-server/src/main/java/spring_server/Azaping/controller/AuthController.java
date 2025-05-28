package spring_server.Azaping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.service.UserService;
import spring_server.Azaping.dto.UuidLoginRequest;
import spring_server.Azaping.dto.UuidLoginResponse;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

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

    private final UserService userService;

    /**
     * UUID 로그인 - 사용자 디바이스 UUID를 등록하고 userId 발급
     * 
     * @param request UUID 로그인 요청 (클라이언트 UUID 포함)
     * @return 발급된 userId를 포함한 응답
     */
    @Operation(summary = "UUID 로그인", 
               description = "클라이언트 디바이스에서 생성한 UUID를 서버에 등록하고 userId를 발급받습니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "로그인 성공 - userId 발급",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UuidLoginResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "success",
                      "userId": 1,
                      "message": null
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 - UUID 형식 오류",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UuidLoginResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "userId": null,
                      "message": "UUID는 필수입니다."
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "서버 내부 오류",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UuidLoginResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "userId": null,
                      "message": "서버 내부 오류가 발생했습니다."
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/uuid-login")
    public ResponseEntity<UuidLoginResponse> uuidLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "UUID 로그인 요청 데이터",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UuidLoginRequest.class),
                    examples = @ExampleObject(
                        value = """
                        {
                          "uuid": "550e8400-e29b-41d4-a716-446655440000"
                        }
                        """
                    )
                )
            )
            @Valid @RequestBody UuidLoginRequest request) {
        try {
            String uuid = request.getUuid();
            
            if (uuid == null || uuid.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(UuidLoginResponse.error("UUID는 필수입니다."));
            }
            
            Long userId = userService.registerOrGetUser(uuid);
            
            log.info("UUID 로그인 성공 - uuid: {}, userId: {}", uuid, userId);
            return ResponseEntity.ok(UuidLoginResponse.success(userId));
            
        } catch (Exception e) {
            log.error("UUID 로그인 실패 - error: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(UuidLoginResponse.error("서버 내부 오류가 발생했습니다."));
        }
    }
} 