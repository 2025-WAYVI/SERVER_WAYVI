# Azaping Server

건강 데이터 수집 및 AI 분석을 위한 멀티 서버 프로젝트

## 프로젝트 구조

- **Spring Server** (`spring-server/`): 사용자 관리 및 건강 데이터 수집 (Spring Boot + JPA + MySQL)
- **AI Server** (`ai-server/`): 건강 데이터 AI 분석 (FastAPI + TensorFlow)

## 주요 기능

- UUID 기반 사용자 등록
- 실시간 건강 데이터 수집 (5분마다)
- 일일 건강 데이터 수집 (하루 1번)
- AI 기반 건강 데이터 분석

## 실행 방법

### Spring Boot 서버
```bash
cd spring-server
./gradlew bootRun
```

### AI 서버
```bash
cd ai-server
python main.py
```

## API 문서

- **Spring Boot Swagger**: http://localhost:8080/swagger-ui.html
- **AI Server FastAPI**: http://localhost:8000/docs 