package spring_server.Azaping.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

/**
 * 이메일 테스트 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class EmailTestController {
    
    private final JavaMailSender mailSender;
    
    /**
     * 이메일 전송 테스트
     */
    @PostMapping("/email")
    public String testEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("kcl2502@gmail.com");
            message.setSubject("[테스트] 이메일 전송 테스트");
            message.setText("이메일 설정이 정상적으로 작동합니다!");
            
            mailSender.send(message);
            log.info("테스트 이메일 전송 성공");
            
            return "이메일 전송 성공! kcl2502@gmail.com을 확인해보세요.";
            
        } catch (Exception e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            return "이메일 전송 실패: " + e.getMessage();
        }
    }
} 