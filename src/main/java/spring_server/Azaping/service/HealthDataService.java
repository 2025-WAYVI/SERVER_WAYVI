package spring_server.Azaping.service;

import spring_server.Azaping.dto.HealthDataRequest;
import spring_server.Azaping.dto.HealthAnalysisResponse;

public interface HealthDataService {
    HealthAnalysisResponse analyzeHealthData(HealthDataRequest request);
} 