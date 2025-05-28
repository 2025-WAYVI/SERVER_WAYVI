// controller/HelloController.java

package spring_server.Azaping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 간단한 헬스체크 컨트롤러
 */
@RestController
public class HelloController {

    /**
     * 서버 상태 확인 엔드포인트
     * @return 간단한 응답 메시지
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello from server!";
    }
}
