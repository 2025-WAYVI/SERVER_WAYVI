# Azaping Server 배포 가이드

## 필수 환경변수

배포 전에 다음 환경변수들을 설정해야 합니다:

### 데이터베이스 설정
```bash
DB_URL=jdbc:mysql://your-db-host:3306/azaping?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
DB_USERNAME=your-db-username
DB_PASSWORD=your-db-password
```

### 서버 설정
```bash
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### AI 서버 설정
```bash
AI_SERVER_URL=http://your-ai-server:8000
```

### 선택적 설정
```bash
# 로깅 레벨
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_SPRING_SERVER=DEBUG

# 보안 (향후 사용)
JWT_SECRET=your-jwt-secret-key-here
ENCRYPTION_KEY=your-encryption-key-here
```

## 배포 방법

### 1. Docker를 사용한 배포
```bash
# 환경변수 파일 생성
cp env.example .env
# .env 파일 수정 후

# Docker 빌드 및 실행
docker build -t azaping-server .
docker run -d --env-file .env -p 8080:8080 azaping-server
```

### 2. JAR 파일을 사용한 배포
```bash
# 빌드
./gradlew build

# 환경변수 설정 후 실행
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:mysql://your-db-host:3306/azaping?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul"
export DB_USERNAME="your-db-username"
export DB_PASSWORD="your-db-password"
export AI_SERVER_URL="http://your-ai-server:8000"

java -jar build/libs/Azaping-0.0.1-SNAPSHOT.jar
```

### 3. 시스템 서비스로 등록 (Linux)
```bash
# /etc/systemd/system/azaping.service 파일 생성
sudo nano /etc/systemd/system/azaping.service

# 서비스 내용:
[Unit]
Description=Azaping Spring Boot Application
After=network.target

[Service]
Type=simple
User=azaping
WorkingDirectory=/opt/azaping
ExecStart=/usr/bin/java -jar /opt/azaping/Azaping-0.0.1-SNAPSHOT.jar
Environment=SPRING_PROFILES_ACTIVE=prod
EnvironmentFile=/opt/azaping/.env
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target

# 서비스 활성화
sudo systemctl enable azaping
sudo systemctl start azaping
sudo systemctl status azaping
```

## 데이터베이스 초기화

프로덕션 환경에서는 `spring.jpa.hibernate.ddl-auto=validate`로 설정되어 있으므로, 
데이터베이스 스키마를 미리 생성해야 합니다.

```sql
-- 데이터베이스 생성
CREATE DATABASE azaping CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여
CREATE USER 'azaping_user'@'%' IDENTIFIED BY 'your-secure-password';
GRANT ALL PRIVILEGES ON azaping.* TO 'azaping_user'@'%';
FLUSH PRIVILEGES;
```

## 모니터링

애플리케이션 상태 확인:
- Health Check: `GET /actuator/health`
- Metrics: `GET /actuator/metrics`
- Info: `GET /actuator/info`

## 보안 고려사항

1. 데이터베이스 비밀번호는 강력한 패스워드 사용
2. 프로덕션에서는 Swagger UI 비활성화 (이미 설정됨)
3. HTTPS 사용 권장
4. 방화벽 설정으로 필요한 포트만 개방
5. 정기적인 보안 업데이트 적용 