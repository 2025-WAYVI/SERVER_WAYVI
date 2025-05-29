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
import spring_server.Azaping.service.HealthDataService;
import spring_server.Azaping.service.UserService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * 건강 리포트 조회 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Health Report", description = "건강 리포트 조회 API")
public class HealthReportController {

    private final HealthDataService healthDataService;
    private final UserService userService;

    /**
     * 건강 리포트 조회
     * 
     * @param userId 사용자 ID (Long 타입, 예: 1)
     * @param date 조회할 날짜 (YYYY-MM-DD 형식, 예: 2024-07-29)
     * @return 해당 날짜의 건강 리포트
     */
    @Operation(summary = "건강 리포트 조회", 
               description = "특정 날짜의 사용자 건강 리포트를 조회합니다. userId는 Long 타입, date는 YYYY-MM-DD 형식입니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "리포트 조회 성공",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "success",
                      "date": "2024-07-29",
                      "userId": 1,
                      "dailyData": {
                        "stepCount": 12050,
                        "activeEnergyBurned": 650.0,
                        "basalEnergyBurned": 1500.0,
                        "averageHeartRate": 75.5,
                        "oxygenSaturation": [97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5],
                        "respiratoryRate": [18.0, 18.0, 18.0, 18.0, 19.0, 18.0, 18.0, 18.0],
                        "bodyTemperature": [36.5, 36.5, 36.6, 36.6, 36.7, 36.6, 36.6, 36.7]
                      },
                      "events": ["정상", "정상", "낙상/충돌"]
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
            responseCode = "404", 
            description = "해당 날짜의 데이터 없음",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "status": "error",
                      "message": "해당 날짜의 건강 데이터를 찾을 수 없습니다."
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
    @GetMapping("/health-report/{userId}/{date}")
    public ResponseEntity<Map<String, Object>> getHealthReport(
            @Parameter(description = "UUID 로그인으로 발급받은 사용자 ID", example = "1")
            @PathVariable("userId") Long userId,
            @Parameter(description = "조회할 날짜 (YYYY-MM-DD 형식)", example = "2024-07-29")
            @PathVariable("date") String date) {
        try {
            // 사용자 존재 여부 확인
            if (!userService.existsByUserId(userId)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "유효하지 않은 사용자입니다.");
                return ResponseEntity.status(401).body(errorResponse);
            }
            
            // 날짜 파싱
            LocalDateTime dateTime = LocalDateTime.parse(date + "T00:00:00");
            
            // 건강 리포트 조회
            Map<String, Object> report = healthDataService.getHealthReport(userId, dateTime);
            
            if (report == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "해당 날짜의 건강 데이터를 찾을 수 없습니다.");
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            log.info("건강 리포트 조회 성공 - userId: {}, date: {}", userId, date);
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            log.error("건강 리포트 조회 실패 - userId: {}, date: {}, error: {}", 
                    userId, date, e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "서버 내부 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
} 