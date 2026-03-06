# Process Monitoring AI - Spring Boot Backend

화학 공정 계측기 실시간 모니터링 및 AI 이상 탐지 서비스의 Spring Boot 백엔드

---

## 기술 스택

| 항목 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| DB | PostgreSQL 16 |
| 인증 | Spring Security + JWT |
| FastAPI 통신 | WebClient (WebFlux) |
| 실시간 스트리밍 | SSE (Server-Sent Events) |

---

## 환경 설정

### 1. 사전 요구사항: Docker Desktop 설치 (Mac)

[Docker Desktop for Mac 다운로드](https://docs.docker.com/desktop/install/mac-install/)

> Apple Silicon(M1/M2/M3)과 Intel Mac 모두 지원합니다. 다운로드 페이지에서 칩에 맞는 버전 선택

설치 후 Docker Desktop 앱 실행 → 상단 메뉴바에 고래 아이콘 뜨면 준비 완료

---

### 2. DB 실행

```bash
# 프로젝트 루트에서 실행
docker-compose up -d
```

**상태 확인:**
```bash
docker ps
# process-monitoring-db 컨테이너가 Up 상태이면 정상
```

**종료:**
```bash
docker-compose down       # 컨테이너만 종료 (데이터 유지)
docker-compose down -v    # 컨테이너 + 데이터 전체 삭제
```

---

### 3. DB 접속 정보

| 항목 | 값 |
|------|----|
| Host | `localhost` |
| Port | `5432` |
| Database | `process_monitoring` |
| Username | `postgres` |
| Password | `password` |

---

### 4. 환경변수 설정

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=password
export JWT_SECRET=your-secret-key-must-be-at-least-256-bits-long
export FASTAPI_URL=http://localhost:8000
```

> 설정하지 않으면 `application.yaml`의 기본값으로 실행됩니다.

---

### 5. 테이블 생성

| 테이블 | 생성 주체 | 시점 |
|--------|----------|------|
| `users` | JPA (Spring Boot) | 앱 기동 시 자동 생성 |
| `events` | `init.sql` | Docker 컨테이너 최초 실행 시 자동 생성 |
| `reports` | `init.sql` | 동일 |
| `inference_logs` | `init.sql` | 동일 |
| `notification_settings` | `init.sql` | 동일 |

`init.sql`은 `CREATE TABLE IF NOT EXISTS`로 작성되어 중복 실행해도 안전합니다.

---

### 6. 앱 실행

```bash
./mvnw spring-boot:run
```

---

## API 목록

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 (JWT 발급) | ❌ |
| GET | `/api/users/me` | 내 정보 조회 | ✅ |
| GET | `/api/sensors/stream` | 계측기 실시간 SSE | ✅ |
| POST | `/api/chat` | RAG 챗봇 | ✅ |
| GET | `/api/reports` | 보고서 목록 | ✅ |
| GET | `/api/reports/{reportId}` | 보고서 상세 | ✅ |

> 자세한 API 명세는 [API_SPEC.md](./API_SPEC.md) 참고

---

## 프로젝트 구조

```
src/main/java/com/skala/process_monitoring_ai/
├── domain/
│   ├── auth/       # 회원가입, 로그인
│   ├── user/       # 유저 정보
│   ├── sensor/     # 계측기 SSE 스트리밍
│   ├── chat/       # RAG 챗봇 프록시
│   └── report/     # 보고서 조회
└── global/
    ├── config/     # Security, WebClient 설정
    ├── security/   # JWT Provider, Filter
    ├── exception/  # 전역 예외 처리
    └── common/     # 공통 응답, BaseEntity
```
