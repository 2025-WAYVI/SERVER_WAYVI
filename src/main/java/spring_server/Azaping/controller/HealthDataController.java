package spring_server.Azaping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_server.Azaping.dto.HealthDataRequest;
import spring_server.Azaping.dto.HealthAnalysisResponse;
import spring_server.Azaping.service.HealthDataService;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Tag(name = "Health Data", description = "건강 데이터 분석 API")
public class HealthDataController {

    private final HealthDataService healthDataService;

    @Operation(summary = "건강 데이터 분석", description = "클라이언트가 수집한 건강 데이터를 분석하고 결과를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "분석 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/analyze")
    public ResponseEntity<HealthAnalysisResponse> analyzeHealthData(
            @RequestBody HealthDataRequest request) {
        HealthAnalysisResponse response = healthDataService.analyzeHealthData(request);
        return ResponseEntity.ok(response);
    }
} 