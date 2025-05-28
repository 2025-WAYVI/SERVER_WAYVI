package spring_server.Azaping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.dto.EmergencyRequest;
import spring_server.Azaping.dto.EmergencyResponse;
import spring_server.Azaping.service.EmergencyService;
import spring_server.Azaping.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

/**
 * 구조요청 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/emergency")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Emergency", description = "구조요청 API")
public class EmergencyController {
    
    private final EmergencyService emergencyService;
    private final UserService userService;
    
    /**
     * 구조요청 전송
     * 프론트엔드에서 사용자 미응답 시 구조요청을 전송합니다.
     * 
     * @param userId 사용자 ID
     * @param request 구조요청 데이터
     * @return 구조요청 처리 결과
     */
    @Operation(summary = "구조요청 전송", 
               description = "프론트엔드에서 사용자 미응답 시 구조요청을 전송합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "구조요청 전송 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmergencyResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "success",
                      "message": "구조요청이 전송되었습니다.",
                      "requestId": "emergency_12345"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 - 필수 필드 누락",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmergencyResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "잘못된 요청입니다. 필수 필드가 누락되었습니다.",
                      "requestId": null
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "유효하지 않은 사용자",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EmergencyResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "유효하지 않은 사용자입니다.",
                      "requestId": null
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
                schema = @Schema(implementation = EmergencyResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "서버 내부 오류가 발생했습니다.",
                      "requestId": null
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/request/{userId}")
    public ResponseEntity<EmergencyResponse> sendEmergencyRequest(
            @Parameter(description = "UUID 로그인으로 발급받은 사용자 ID", example = "1")
            @PathVariable("userId") Long userId,
            @Valid @RequestBody EmergencyRequest request) {
        try {
            // 사용자 존재 여부 확인
            if (!userService.existsByUserId(userId)) {
                return ResponseEntity.status(401)
                    .body(EmergencyResponse.error("유효하지 않은 사용자입니다."));
            }
            
            // 구조요청 처리
            EmergencyResponse response = emergencyService.processEmergencyRequest(userId, request);
            
            log.info("구조요청 접수 - userId: {}, event: {}", userId, request.getEvent());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("구조요청 처리 실패 - userId: {}, error: {}", userId, e.getMessage());
            
            return ResponseEntity.internalServerError()
                .body(EmergencyResponse.error("서버 내부 오류가 발생했습니다."));
        }
    }
} 