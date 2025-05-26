package spring_server.Azaping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.dto.HealthDataRequest;
import spring_server.Azaping.dto.HealthDataResponse;
import spring_server.Azaping.service.HealthDataService;

/**
 * 건강 데이터 제출 API 컨트롤러
 * 실시간 심박수 데이터(5분마다)와 일일 건강 데이터(하루 1번) 수신
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Health Data", description = "건강 데이터 제출 API")
public class HealthDataController {

    private final HealthDataService healthDataService;

    /**
     * 건강 데이터 제출
     * - 실시간 심박수 데이터 (5분마다)
     * - 일일 건강 데이터 (하루 1번)
     * 
     * @param request 건강 데이터 요청 (userId, 타임스탬프, 데이터 타입, 건강 데이터 포함)
     * @return 데이터 수신 확인 응답
     */
    @Operation(summary = "건강 데이터 제출", 
               description = "UUID 로그인으로 발급받은 userId를 포함하여 건강 데이터를 제출합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "데이터 수신 성공 - 처리 대기 중"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 - 필수 필드 누락, 잘못된 형식"),
        @ApiResponse(responseCode = "401", description = "인증 실패 - UUID/userId 없음 또는 유효하지 않음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/health-data")
    public ResponseEntity<HealthDataResponse> submitHealthData(@Validated @RequestBody HealthDataRequest request) {
        log.info("건강 데이터 제출 요청: userId={}, dataType={}", request.getUserId(), request.getDataType());
        
        try {
            // 건강 데이터 저장 처리
            healthDataService.saveHealthData(request);
            
            log.info("건강 데이터 제출 성공: userId={}, dataType={}", request.getUserId(), request.getDataType());
            
            // 202 Accepted - 데이터가 성공적으로 수신되어 처리 대기 중
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(HealthDataResponse.success());
            
        } catch (IllegalArgumentException e) {
            // 401 Unauthorized - 유효하지 않은 사용자
            log.warn("유효하지 않은 사용자: userId={}, error={}", request.getUserId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(HealthDataResponse.error("유효하지 않은 사용자입니다."));
            
        } catch (Exception e) {
            // 500 Internal Server Error
            log.error("건강 데이터 제출 실패: userId={}, error={}", request.getUserId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HealthDataResponse.error("서버 내부 오류가 발생했습니다."));
        }
    }
} 