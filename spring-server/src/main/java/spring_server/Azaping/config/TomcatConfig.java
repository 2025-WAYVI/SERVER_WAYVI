package spring_server.Azaping.config;

import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat 서버 설정
 * HTTP 헤더 크기 제한 등을 설정
 */
@Configuration
public class TomcatConfig {

    /**
     * Tomcat 서버 설정 커스터마이징
     * HTTP 헤더 크기 제한을 늘려서 Swagger UI 오류 해결
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof Http11NioProtocol) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                // HTTP 헤더 크기를 2MB로 설정 (더 크게)
                protocol.setMaxHttpHeaderSize(2 * 1024 * 1024);
                // 연결 타임아웃을 20초로 설정
                protocol.setConnectionTimeout(20000);
            }
        });
    }
} 