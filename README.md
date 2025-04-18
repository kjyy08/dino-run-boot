# 🦖 Dino Run - 프로젝트 가이드

이 문서는 Dino Run 프로젝트의 개발, 테스트, 배포에 관한 상세 가이드라인을 제공합니다.

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [사용자 시나리오](#사용자-시나리오)
- [프로젝트 아키텍처](#프로젝트-아키텍처)
- [개발 가이드라인](#개발-가이드라인)
- [테스트 가이드](#테스트-가이드)
- [배포 가이드](#배포-가이드)

## 프로젝트 개요

Dino Run은 구글 크롬의 T-Rex 게임을 모티브로 한 웹 기반 게임 애플리케이션입니다. 사용자는 공룡 캐릭터를 조작하여 장애물을 피하고, 최고 점수를 기록할 수 있습니다. 이 프로젝트는 Unity로 개발된
게임과 Spring Boot 백엔드를 결합하여 게임 플레이, 점수 저장, 랭킹 시스템을 제공합니다.

### 주요 기능

- Unity WebGL 기반의 공룡 달리기 게임
- 사용자 점수 저장 및 관리
- 상위 20명의 랭킹 시스템

## 사용자 시나리오

### 1. 게임 플레이

1. 사용자가 웹 브라우저를 통해 애플리케이션에 접속합니다.
2. 메인 페이지에서 Unity 기반의 공룡 달리기 게임이 로드됩니다.
3. 사용자는 스페이스바를 이용해 장애물을 피하며 게임을 플레이합니다.
4. 장애물에 부딪히면 게임이 종료되고 획득한 점수가 화면에 표시됩니다.

### 2. 점수 등록

1. 게임 종료 후, 사용자는 자신의 닉네임을 입력하여 점수를 등록할 수 있습니다.
2. 시스템은 입력된 닉네임과 획득한 점수를 저장합니다.
3. 닉네임이 이미 존재하는 경우, 기존 점수를 새로운 점수로 업데이트합니다.
4. 점수 등록이 완료되면 성공 메시지가 표시됩니다.

### 3. 랭킹 확인

1. 메인 페이지에서 상위 20명의 랭킹 목록을 확인할 수 있습니다.
2. 랭킹은 점수 순으로 정렬되어 표시됩니다.
3. 사용자는 자신의 점수가 랭킹에 어떻게 반영되었는지 확인할 수 있습니다.

## 프로젝트 아키텍처

### 전체 아키텍처

Dino Run은 Spring Boot 기반의 웹 애플리케이션으로, 다음과 같은 계층 구조를 가지고 있습니다:

```
+------------------+
|     Client       |
| (Web Browser)    |
+--------+---------+
         |
         v
+--------+---------+
|   Presentation   |
|     Layer        |
| (Controllers)    |
+--------+---------+
         |
         v
+--------+---------+
|   Business       |
|     Layer        |
| (Services)       |
+--------+---------+
         |
         v
+--------+---------+
|   Data Access    |
|     Layer        |
| (Repositories)   |
+--------+---------+
         |
         v
+--------+---------+
|   Database       |
| (MySQL)          |
+------------------+
```

### 계층별 설명

#### 1. 프레젠테이션 계층 (Presentation Layer)

- **컨트롤러 (Controllers)**: 클라이언트의 요청을 처리하고 응답을 반환합니다.
    - `MainController`: 메인 페이지를 제공하는 뷰 컨트롤러
    - `ScoreController`: 점수 관련 API를 제공하는 REST 컨트롤러

#### 2. 비즈니스 계층 (Business Layer)

- **서비스 (Services)**: 비즈니스 로직을 처리합니다.
    - `ScoreService`: 점수 저장, 조회 등의 비즈니스 로직 처리

#### 3. 데이터 액세스 계층 (Data Access Layer)

- **리포지토리 (Repositories)**: 데이터베이스와의 상호작용을 담당합니다.
    - `ScoreRepository`: 점수 데이터의 CRUD 작업 수행

#### 4. 데이터 모델 (Data Model)

- **엔티티 (Entities)**: 데이터베이스 테이블과 매핑되는 객체입니다.
    - `Score`: 사용자 닉네임과 점수 정보를 저장하는 엔티티

#### 5. 데이터 전송 객체 (DTOs)

- **요청/응답 DTO**: 클라이언트와 서버 간의 데이터 전송을 위한 객체입니다.
    - `ScoreRequestDTO`: 점수 저장 요청 데이터
    - `ScoreResponseDTO`: 점수 조회 응답 데이터

### 데이터 흐름

1. 클라이언트가 점수 등록 요청을 보냅니다.
2. 컨트롤러가 요청을 받아 서비스 계층으로 전달합니다.
3. 서비스 계층에서 비즈니스 로직을 처리하고 리포지토리를 통해 데이터를 저장합니다.
4. 저장 결과를 컨트롤러로 반환하고, 컨트롤러는 클라이언트에게 응답을 전송합니다.

### 기술 스택

- **`Client`**
    - Unity 6 (2D)
        - WebGL 빌드
    - HTML/CSS/JavaScript
    - Bootstrap 5.3
    - AOS (Animate On Scroll)

- **`Server`**
    - Spring Boot 3.4.4
        - Spring MVC (REST API)
        - Spring Data JPA
        - Spring Security
        - Thymeleaf

- **`DataBase`**
    - MySQL (운영 환경)
    - H2 (테스트 환경)

## 개발 가이드라인

### 프로젝트 설정

#### 필수 요구사항

- JDK 17 이상
- Gradle 8.0 이상
- MySQL 8.0 이상
- Unity 6 (게임 개발 시)

#### 개발 환경 설정

1. 프로젝트 클론:
   ```bash
   git clone https://github.com/yourusername/dino-run-boot.git
   cd dino-run-boot
   ```

2. 환경 변수 설정:
   ```
   DB_URL=jdbc:mysql://localhost:3306/dino_run
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

3. 데이터베이스 생성:
   ```sql
   CREATE DATABASE dino_run CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. 애플리케이션 실행:
   ```bash
   ./gradlew bootRun
   ```

### 코드 스타일 가이드

#### 일반 규칙

- 클래스 이름: PascalCase (예: `ScoreController`)
- 메서드 이름: camelCase (예: `saveScore`)
- 변수 이름: camelCase (예: `scoreRepository`)
- 상수: UPPER_SNAKE_CASE (예: `MAX_SCORE_LIMIT`)

#### 패키지 구조

```
com.juyb99.dinorunboot
├── common
│   └── config
├── controller
│   ├── api
│   └── view
├── dto
│   ├── request
│   └── response
├── model
├── repository
└── service
```

#### 코드 작성 지침

1. **단일 책임 원칙(SRP)**: 각 클래스는 하나의 책임만 가져야 합니다.
2. **의존성 주입**: 생성자 주입 방식을 사용합니다.
3. **예외 처리**: 적절한 예외 처리와 로깅을 구현합니다.
4. **테스트 코드**: 모든 기능에 대한 단위 테스트를 작성합니다.

### API 설계 가이드

#### REST API 엔드포인트

- `GET /api/scores`: 모든 점수 조회 (페이지네이션 지원)
- `GET /api/scores/{id}`: 특정 ID의 점수 조회
- `POST /api/scores`: 새로운 점수 저장

#### 요청/응답 형식

- 요청 본문 예시:
  ```json
  {
    "nickname": "player1",
    "point": 1500
  }
  ```

- 응답 본문 예시:
  ```json
  {
    "scoreId": 1,
    "nickname": "player1",
    "point": 1500
  }
  ```

## 테스트 가이드

### 테스트 환경 설정

- 테스트는 H2 인메모리 데이터베이스를 사용합니다.
- `src/test/resources/application-test.yml` 파일에 테스트 환경 설정이 포함되어 있습니다.

### 테스트 종류

#### 단위 테스트

- 리포지토리 테스트: `ScoreRepositoryTest`
- 서비스 테스트: `ScoreServiceTest`
- 컨트롤러 테스트: `ScoreControllerTest`

#### 통합 테스트

- 애플리케이션 컨텍스트 로드 테스트: `DinoRunBootApplicationTests`

### 테스트 실행 방법

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "com.juyb99.dinorunboot.service.ScoreServiceTest"
```

### 테스트 작성 지침

1. **테스트 격리**: 각 테스트는 독립적으로 실행될 수 있어야 합니다.
2. **테스트 명명**: 테스트 메서드 이름은 `should_ExpectedBehavior_When_StateUnderTest` 형식을 따릅니다.
3. **AAA 패턴**: Arrange(준비), Act(실행), Assert(검증) 패턴을 사용합니다.
4. **모의 객체**: 필요한 경우 Mockito를 사용하여 의존성을 모의(mock)합니다.

## 배포 가이드

### 배포 환경 요구사항

- JDK 17 이상
- MySQL 8.0 이상
- 최소 1GB RAM, 10GB 디스크 공간

### 배포 절차

#### 1. 애플리케이션 빌드

```bash
./gradlew clean build
```

#### 2. JAR 파일 실행

```bash
java -jar build/libs/dino-run-boot-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### 3. Docker를 이용한 배포

```bash
# Docker 이미지 빌드
docker build -t dino-run-boot .

# Docker 컨테이너 실행
docker run -d -p 8080:8080 \
  -e DB_URL=jdbc:mysql://db-host:3306/dino_run \
  -e DB_USERNAME=prod_user \
  -e DB_PASSWORD=prod_password \
  --name dino-run-app \
  dino-run-boot
```

### 환경별 설정

- 개발 환경: `application-dev.yml`
- 운영 환경: `application-prod.yml`

### 모니터링 및 로깅

- 애플리케이션 로그는 기본적으로 콘솔에 출력됩니다.
- 운영 환경에서는 로그 파일을 설정하여 로그를 저장하는 것을 권장합니다.
- 필요한 경우 Prometheus, Grafana 등의 모니터링 도구를 통합할 수 있습니다.

### 백업 및 복구

- 데이터베이스 백업은 정기적으로 수행해야 합니다.
- MySQL 백업 명령어:
  ```bash
  mysqldump -u username -p dino_run > backup.sql
  ```

- 복구 명령어:
  ```bash
  mysql -u username -p dino_run < backup.sql
  ```

## 문제 해결 및 지원

### 알려진 이슈

- 현재 알려진 주요 이슈가 없습니다.

### 지원 및 문의

- 이슈 트래커: GitHub Issues
- 이메일 지원: support@example.com

---

이 문서는 Dino Run 프로젝트의 개발 및 운영에 관한 가이드라인을 제공합니다. 문서의 내용은 프로젝트의 발전에 따라 지속적으로 업데이트될 예정입니다.