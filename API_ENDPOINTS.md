# Azaping API 엔드포인트 가이드

## 🌐 Base URL

### 개발 환경
```
http://localhost:8080
```

### 프로덕션 환경 (AWS Elastic Beanstalk)
```
http://wayvi-server.ap-northeast-2.elasticbeanstalk.com
```
> **상태**: ✅ 배포 완료 및 서비스 중
> **Health Check**: http://wayvi-server.ap-northeast-2.elasticbeanstalk.com/actuator/health

## 📚 API 문서

### Swagger UI
프로덕션 환경에서는 보안상 비활성화되어 있습니다. 개발 환경에서만 사용 가능합니다.
- 개발 환경: `http://localhost:8080/swagger-ui.html`

### Health Check
서버 상태 확인:
```
GET {BASE_URL}/actuator/health
```

## 🔧 주요 API 엔드포인트

### 건강 데이터 관련 (테스트 완료 ✅)
```
POST {BASE_URL}/api/v1/health-data/daily/{userId}
POST {BASE_URL}/api/v1/health-data/realtime/{userId}
```

### 응급 상황 관련 (테스트 완료 ✅)
```
POST {BASE_URL}/api/v1/emergency/request/{userId}
```

### 건강 리포트 관련
```
GET {BASE_URL}/api/v1/health-report/{userId}/{date}
```

### 인증 관련
```
GET {BASE_URL}/api/v1/auth/uuid-login
```

> **참고**: 위 API들은 실제 서버에서 테스트되었으며, 현재 정상 작동 중입니다.

## 📱 클라이언트 설정 예제

### Android (Kotlin)
```kotlin
class ApiConfig {
    companion object {
        // 개발 환경
        // const val BASE_URL = "http://10.0.2.2:8080/"
        
        // 프로덕션 환경
        const val BASE_URL = "http://wayvi-server.ap-northeast-2.elasticbeanstalk.com/"
    }
}
```

### iOS (Swift)
```swift
struct APIConfig {
    // 개발 환경
    // static let baseURL = "http://localhost:8080"
    
    // 프로덕션 환경
    static let baseURL = "http://wayvi-server.ap-northeast-2.elasticbeanstalk.com"
}
```

### React Native / JavaScript
```javascript
const API_CONFIG = {
  // 개발 환경
  // BASE_URL: 'http://localhost:8080',
  
  // 프로덕션 환경
  BASE_URL: 'http://wayvi-server.ap-northeast-2.elasticbeanstalk.com',
};
```

## 🔐 인증

현재는 JWT 인증이 구현되지 않았습니다. 향후 업데이트 예정입니다.

## 📋 응답 형식

### 성공 응답
```json
{
  "success": true,
  "data": {
    // 실제 데이터
  },
  "message": "성공 메시지"
}
```

### 에러 응답
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```

## 📞 지원

- 기술 문의: [개발팀 이메일]
- API 관련 이슈: GitHub Issues 활용 