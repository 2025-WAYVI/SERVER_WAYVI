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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.dto.RealtimeHealthDataRequest;
import spring_server.Azaping.dto.DailyHealthDataRequest;
import spring_server.Azaping.dto.RealtimeHealthDataResponse;
import spring_server.Azaping.dto.DailyHealthDataResponse;
import spring_server.Azaping.service.HealthDataService;
import spring_server.Azaping.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * 건강 데이터 제출 API 컨트롤러
 * 실시간 심박수 데이터(5분마다)와 일일 건강 데이터(하루 1번) 수신
 */
@RestController
@RequestMapping("/api/v1/health-data")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Health Data", description = "건강 데이터 제출 API")
public class HealthDataController {

    private final HealthDataService healthDataService;
    private final UserService userService;

    /**
     * 실시간 건강 데이터 제출 (5분에 1번)
     * 
     * @param userId 사용자 ID
     * @param request 실시간 건강 데이터 요청
     * @return 데이터 수신 확인 응답 (이상 탐지 결과 포함)
     */
    @Operation(summary = "실시간 건강 데이터 제출", 
               description = "UUID 로그인으로 발급받은 userId를 포함하여 실시간 건강 데이터를 제출합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202", 
            description = "데이터 수신 성공 - 처리 대기 중",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RealtimeHealthDataResponse.class),
                examples = {
                    @ExampleObject(
                        name = "정상 상태",
                        value = """
                        {
                          "status": "success",
                          "message": "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다.",
                          "event": "정상"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "이상 탐지",
                        value = """
                        {
                          "status": "success",
                          "message": "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다.",
                          "event": "낙상/충돌"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 - 필수 필드 누락, 잘못된 형식",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RealtimeHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "잘못된 요청입니다. 필수 필드가 누락되었습니다.",
                      "event": null
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "인증 실패 - UUID/userId 없음 또는 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RealtimeHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "유효하지 않은 사용자입니다.",
                      "event": null
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
                schema = @Schema(implementation = RealtimeHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "서버 내부 오류가 발생했습니다.",
                      "event": null
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/realtime/{userId}")
    public ResponseEntity<RealtimeHealthDataResponse> submitRealtimeHealthData(
            @Parameter(description = "UUID 로그인으로 발급받은 사용자 ID", example = "1")
            @PathVariable("userId") Long userId,
            @Valid @RequestBody RealtimeHealthDataRequest request) {
        try {
            // 사용자 존재 여부 확인
            if (!userService.existsByUserId(userId)) {
                return ResponseEntity.status(401)
                    .body(RealtimeHealthDataResponse.error("유효하지 않은 사용자입니다."));
            }
            
            // 실시간 건강 데이터 처리 (AI 분석 포함)
            String eventResult = healthDataService.processRealtimeHealthData(userId, request);
            
            log.info("실시간 건강 데이터 제출 성공 - userId: {}, event: {}", userId, eventResult);
            
            return ResponseEntity.accepted().body(RealtimeHealthDataResponse.success(eventResult));
            
        } catch (Exception e) {
            log.error("실시간 건강 데이터 제출 실패 - userId: {}, error: {}", userId, e.getMessage());
            
            return ResponseEntity.internalServerError()
                .body(RealtimeHealthDataResponse.error("서버 내부 오류가 발생했습니다."));
        }
    }

    /**
     * 일일 건강 데이터 제출 (하루 1번)
     * 
     * @param userId 사용자 ID
     * @param request 일일 건강 데이터 요청
     * @return 데이터 수신 확인 응답
     */
    @Operation(summary = "일일 건강 데이터 제출", 
               description = "UUID 로그인으로 발급받은 userId를 포함하여 하루 1번, 일일 건강 데이터를 제출합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202", 
            description = "일일 데이터 수신 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DailyHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "success",
                      "message": "일일 건강 데이터가 성공적으로 수신되었습니다."
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 - 필수 필드 누락, 잘못된 형식",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DailyHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "잘못된 요청입니다. 필수 필드가 누락되었습니다."
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "인증 실패 - UUID/userId 없음 또는 유효하지 않음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DailyHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "유효하지 않은 사용자입니다."
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
                schema = @Schema(implementation = DailyHealthDataResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "서버 내부 오류가 발생했습니다."
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/daily/{userId}")
    public ResponseEntity<DailyHealthDataResponse> submitDailyHealthData(
            @Parameter(description = "UUID 로그인으로 발급받은 사용자 ID", example = "1")
            @PathVariable("userId") Long userId,
            @Valid @RequestBody DailyHealthDataRequest request) {
        try {
            // 사용자 존재 여부 확인
            if (!userService.existsByUserId(userId)) {
                return ResponseEntity.status(401)
                    .body(DailyHealthDataResponse.error("유효하지 않은 사용자입니다."));
            }
            
            // 일일 건강 데이터 처리
            healthDataService.processDailyHealthData(userId, request);
            
            log.info("일일 건강 데이터 제출 성공 - userId: {}", userId);
            
            return ResponseEntity.accepted().body(DailyHealthDataResponse.success());
            
        } catch (Exception e) {
            log.error("일일 건강 데이터 제출 실패 - userId: {}, error: {}", userId, e.getMessage());
            
            return ResponseEntity.internalServerError()
                .body(DailyHealthDataResponse.error("서버 내부 오류가 발생했습니다."));
        }
    }
} 