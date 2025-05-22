package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import spring_server.Azaping.dto.HealthDataRequest;
import spring_server.Azaping.dto.HealthAnalysisResponse;

@Service
@RequiredArgsConstructor
public class HealthDataServiceImpl implements HealthDataService {
    
    private final WebClient webClient;
    
    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Override
    public HealthAnalysisResponse analyzeHealthData(HealthDataRequest request) {
        // 데이터 유효성 검증
        validateHealthData(request);

        // AI 서버로 데이터 전송 및 분석 결과 수신
        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HealthAnalysisResponse.class)
                .block();
    }

    private void validateHealthData(HealthDataRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (request.getTimestamp() == null) {
            throw new IllegalArgumentException("Timestamp is required");
        }
        // 추가적인 유효성 검증 로직 구현
    }
} 