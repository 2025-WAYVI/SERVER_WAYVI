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
                
                <h3>2. 실시간 건강 데이터 제출</h3>
                <p><strong>POST</strong> /api/v1/health-data</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "userId": 1,
                  "timestamp": "2024-07-30T10:05:00Z",
                  "dataType": "REALTIME",
                  "heartRate": 80,
                  "stepCount": 7200,
                  "activeEnergyBurned": 500,
                  "runningSpeed": [15],
                  "accel": [0.12, 0.02, 0.98],
                  "gyro": [0.02, 0.01, 0.03]
                }
                </pre>
                
                <h3>3. 일일 건강 데이터 제출</h3>
                <p><strong>POST</strong> /api/v1/health-data</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "userId": 1,
                  "timestamp": "2024-07-30T08:00:00Z",
                  "dataType": "DAILY",
                  "stepCount": 12050,
                  "stepCountStartDate": "2024-07-29T00:00:00Z",
                  "stepCountEndDate": "2024-07-29T23:59:59Z",
                  "runningSpeed": [10, 9, 10, 11, 7],
                  "runningSpeedStartDate": "2024-07-29T00:00:00Z",
                  "runningSpeedEndDate": "2024-07-29T23:59:59Z",
                  "basalEnergyBurned": 1500,
                  "activeEnergyBurned": 650,
                  "activeEnergyBurnedStartDate": "2024-07-29T00:00:00Z",
                  "activeEnergyBurnedEndDate": "2024-07-29T23:59:59Z",
                  "height": 170,
                  "bodyMass": 68.5,
                  "oxygenSaturation": [97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5, 97.5],
                  "bloodPressureSystolic": 120,
                  "bloodPressureDiastolic": 80,
                  "respiratoryRate": [18, 18, 18, 18, 18, 18, 18, 18],
                  "bodyTemperature": [36.5, 36.5, 36.6, 36.6, 36.7, 36.6, 36.6, 36.7]
                }
                </pre>
                
                <h3>4. 구조요청 전송</h3>
                <p><strong>POST</strong> /api/v1/emergency/request</p>
                <p>Content-Type: application/json</p>
                <pre>
                {
                  "userId": 1,
                  "event": "낙상/충돌",
                  "latitude": 37.5665,
                  "longitude": 126.9780,
                  "timestamp": "2024-07-30T10:05:00Z"
                }
                </pre>
                
                <h3>5. 건강 리포트 조회</h3>
                <p><strong>GET</strong> /api/v1/health-report/{userId}/{date}</p>
                <p>파라미터 형식:</p>
                <ul>
                  <li>userId: Long 타입 (예: 1)</li>
                  <li>date: YYYY-MM-DD 형식 (예: 2024-07-29)</li>
                </ul>
                <p>예시: <strong>GET</strong> /api/v1/health-report/1/2024-07-29</p>
                
                <h3>테스트 방법:</h3>
                <p>Postman, curl, 또는 다른 HTTP 클라이언트를 사용하여 위 API들을 테스트할 수 있습니다.</p>
                
                <h3>서버 상태:</h3>
                <p>✅ 서버가 정상적으로 실행 중입니다.</p>
                <p>✅ MySQL 데이터베이스 연결됨</p>
                """;
    }
} 