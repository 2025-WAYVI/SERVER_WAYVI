package spring_server.Azaping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 이메일 전송 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${emergency.mail.recipient}")
    private String emergencyMailRecipient;
    
    /**
     * 구조요청 이메일 전송
     */
    public void sendEmergencyEmail(Long userId, String event, Double latitude, Double longitude, LocalDateTime timestamp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emergencyMailRecipient);
            message.setSubject("[긴급] 구조요청 - " + event);
            
            String body = String.format(
                "긴급 구조요청이 접수되었습니다.\n\n" +
                "사용자 ID: %s\n" +
                "상황: %s\n" +
                "위치: 위도 %.6f, 경도 %.6f\n" +
                "발생 시각: %s\n\n" +
                "즉시 대응이 필요합니다.",
                userId, event, latitude, longitude, 
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            
            message.setText(body);
            
            mailSender.send(message);
            log.info("구조요청 이메일 전송 완료 - userId: {}, event: {}", userId, event);
            
        } catch (Exception e) {
            log.error("구조요청 이메일 전송 실패 - userId: {}, error: {}", userId, e.getMessage());
        }
    }
} 