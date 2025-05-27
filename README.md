# Azaping Server

건강 데이터 수집 및 AI 분석을 위한 멀티 서버 프로젝트

## 프로젝트 구조

```
Azaping_server_new/
├── ai-server/              # AI 서버 (FastAPI + TensorFlow)
│   ├── app.py             # AI 분석 API
│   └── model/             # 머신러닝 모델
├── spring-server/          # Spring Boot 서버
│   ├── src/               # Java 소스 코드
│   ├── build.gradle       # Gradle 설정
│   ├── gradlew           # Gradle Wrapper
│   └── 기타 Spring Boot 파일들
├── README.md              # 프로젝트 문서
└── 기타 설정 파일들
```

## 서버별 역할

### 🤖 AI Server (`ai-server/`)
- **기술 스택**: FastAPI + TensorFlow
- **역할**: 건강 데이터 이상 탐지 및 AI 분석
- **포트**: 8000 (기본값)

### 🌱 Spring Server (`spring-server/`)
- **기술 스택**: Spring Boot + JPA + MySQL
- **역할**: UUID 기반 사용자 관리 및 건강 데이터 수집
- **포트**: 8080 (기본값)

## 개요

이 프로젝트는 클라이언트 디바이스에서 생성한 UUID를 기반으로 사용자를 식별하고, 건강 데이터를 수집하여 AI 분석을 수행하는 멀티 서버 시스템입니다.

## 주요 기능

- **UUID 기반 사용자 등록**: 클라이언트 UUID를 서버 userId로 변환
- **실시간 건강 데이터 수집**: 5분마다 심박수 데이터 수신
- **일일 건강 데이터 수집**: 하루 1번 종합 건강 데이터 수신
- **AI 기반 이상 탐지**: 수집된 데이터를 AI 서버로 전송하여 분석
- **데이터 유효성 검증**: 사용자 및 데이터 타입별 필수 필드 검증

## 실행 방법

### 1. Spring Boot 서버 실행

```bash
cd spring-server
./gradlew bootRun
```

### 2. AI 서버 실행

```bash
cd ai-server
python app.py
```

### 3. API 문서 확인

- **Spring Boot Swagger**: http://localhost:8080/swagger-ui.html
- **AI Server**: http://localhost:8000/docs

## Spring Boot 서버 상세 정보

## 기술 스택

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **MySQL 8.0**
- **Lombok**
- **SpringDoc OpenAPI (Swagger)**

## API 명세

### 기본 정보

- **Base URL**: `http://localhost:8080`
- **API Version**: v1
- **인증**: 없음 (UUID 기반 식별)

### 1. UUID 로그인

사용자 디바이스 UUID를 등록하고 서버 userId를 발급받습니다.

```http
POST /api/v1/auth/uuid-login
Content-Type: application/json

{
  "uuid": "550e8400-e29b-41d4-a716-446655440000"
}
```

**응답 (200 OK)**:
```json
{
  "status": "success",
  "userId": "user_001"
}
```

### 2. 건강 데이터 제출

#### 실시간 심박수 데이터 (5분마다)

```http
POST /api/v1/health-data
Content-Type: application/json

{
  "userId": "user_001",
  "timestamp": "2024-07-30T10:05:00Z",
  "dataType": "REALTIME",
  "heartRate": 80
}
```

#### 일일 건강 데이터 (하루 1번)

```http
POST /api/v1/health-data
Content-Type: application/json

{
  "userId": "user_001",
  "timestamp": "2024-07-30T08:00:00Z",
  "dataType": "DAILY",
  "stepCount": 12050,
  "stepCountStartDate": "2024-07-29T00:00:00Z",
  "stepCountEndDate": "2024-07-29T23:59:59Z",
  "runningSpeed": 5,
  "runningSpeedStartDate": "2024-07-29T00:00:00Z",
  "runningSpeedEndDate": "2024-07-29T23:59:59Z",
  "basalEnergyBurned": 1500,
  "activeEnergyBurned": 650,
  "activeEnergyBurnedStartDate": "2024-07-29T00:00:00Z",
  "activeEnergyBurnedEndDate": "2024-07-29T23:59:59Z",
  "height": 170,
  "bodyMass": 68.5,
  "oxygenSaturation": 97.5,
  "bloodPressureSystolic": 120,
  "bloodPressureDiastolic": 80,
  "respiratoryRate": 18,
  "bodyTemperature": 36.5
}
```

**응답 (202 Accepted)**:
```json
{
  "status": "success",
  "message": "건강 데이터가 성공적으로 수신되어 처리 대기 중입니다."
}
```

## 프로젝트 구조

```
src/main/java/spring_server/Azaping/
├── controller/          # REST API 컨트롤러
│   ├── AuthController.java
│   └── HealthDataController.java
├── service/            # 비즈니스 로직
│   ├── AuthService.java
│   ├── AuthServiceImpl.java
│   ├── HealthDataService.java
│   └── HealthDataServiceImpl.java
├── repository/         # 데이터 접근 계층
│   ├── UserRepository.java
│   └── HealthDataRepository.java
├── entity/            # JPA 엔티티
│   ├── User.java
│   └── HealthData.java
├── dto/               # 데이터 전송 객체
│   ├── UuidLoginRequest.java
│   ├── UuidLoginResponse.java
│   ├── HealthDataRequest.java
│   └── HealthDataResponse.java
└── AzapingApplication.java
```

## 데이터베이스 스키마

### users 테이블
- `id`: 기본키 (AUTO_INCREMENT)
- `uuid`: 클라이언트 UUID (UNIQUE)
- `user_id`: 서버 발급 사용자 ID (UNIQUE, user_001 형식)
- `created_at`: 등록 시각
- `last_active_at`: 마지막 활동 시각

### health_data 테이블
- `id`: 기본키 (AUTO_INCREMENT)
- `user_id`: 사용자 ID (users.user_id 참조)
- `timestamp`: 데이터 제출 시각
- `data_type`: 데이터 타입 (REALTIME/DAILY)
- `heart_rate`: 심박수 (실시간 데이터)
- `step_count`: 걸음 수 (일일 데이터)
- `running_speed`: 달리기 속도
- `basal_energy_burned`: 기초 대사량
- `active_energy_burned`: 활동 칼로리
- `height`: 신장
- `body_mass`: 체중
- `oxygen_saturation`: 산소포화도
- `blood_pressure_systolic`: 수축기 혈압
- `blood_pressure_diastolic`: 이완기 혈압
- `respiratory_rate`: 호흡수
- `body_temperature`: 체온
- `created_at`: 데이터 생성 시각

## 오류 코드

| HTTP 상태 코드 | 설명 |
|---------------|------|
| 200 | 요청 성공 |
| 202 | 요청이 성공적으로 접수됨 |
| 400 | 잘못된 요청 (필수 필드 누락, 잘못된 형식 등) |
| 401 | 인증 실패 (UUID/userId 없음 또는 유효하지 않음) |
| 500 | 서버 내부 오류 |

## 개발자 정보

- **프로젝트명**: Azaping Server
- **버전**: 0.0.1-SNAPSHOT
- **개발 환경**: Java 17, Spring Boot 3.4.5 