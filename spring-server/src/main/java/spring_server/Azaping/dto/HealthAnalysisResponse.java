package spring_server.Azaping.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HealthAnalysisResponse {
    private String userId;
    private LocalDateTime analysisTimestamp;
    private String overallHealthStatus;
    private List<String> insights;
    private List<String> recommendations;
    private Double healthScore;
    private String riskLevel;
} 