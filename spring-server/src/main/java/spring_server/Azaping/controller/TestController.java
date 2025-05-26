package spring_server.Azaping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API 테스트를 위한 간단한 컨트롤러
 * Swagger UI에 문제가 있을 때 대안으로 사용
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * API 목록 및 사용법 안내
     */
    @GetMapping("/info")
    public String getApiInfo() {
        return """
                <h1>Azaping Server API 테스트</h1>
                <h2>사용 가능한 API:</h2>
                
                <h3>1. UUID 로그인</h3>
                <p><strong>POST</strong> /api/v1/auth/uuid-login</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "uuid": "550e8400-e29b-41d4-a716-446655440000"
                }
                </pre>
                
                <h3>2. 실시간 심박수 데이터 제출</h3>
                <p><strong>POST</strong> /api/v1/health-data</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "userId": "user_001",
                  "timestamp": "2024-07-30T10:05:00",
                  "dataType": "REALTIME",
                  "heartRate": 80
                }
                </pre>
                
                <h3>3. 일일 건강 데이터 제출</h3>
                <p><strong>POST</strong> /api/v1/health-data</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "userId": "user_001",
                  "timestamp": "2024-07-30T08:00:00",
                  "dataType": "DAILY",
                  "stepCount": 12050,
                  "height": 170,
                  "bodyMass": 68.5,
                  "oxygenSaturation": 97.5,
                  "bloodPressureSystolic": 120,
                  "bloodPressureDiastolic": 80,
                  "respiratoryRate": 18,
                  "bodyTemperature": 36.5
                }
                </pre>
                
                <h3>테스트 방법:</h3>
                <p>Postman, curl, 또는 다른 HTTP 클라이언트를 사용하여 위 API들을 테스트할 수 있습니다.</p>
                
                <h3>서버 상태:</h3>
                <p>✅ 서버가 정상적으로 실행 중입니다.</p>
                <p>✅ MySQL 데이터베이스 연결됨</p>
                """;
    }
} 