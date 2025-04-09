# 1. 빌드 스테이지
FROM openjdk:17-jdk-slim AS builder

# 2. 작업 디렉토리 설정 (컨테이너 내부 디렉토리)
WORKDIR /app

# 3. Gradle Wrapper 및 빌드 파일 복사
COPY gradlew /app/gradlew
COPY gradlew.bat /app/gradlew.bat
COPY gradle /app/gradle/

# 4. 프로젝트 빌드 파일 및 소스 복사
COPY build.gradle settings.gradle /app/
COPY src /app/src/

# 5. 실행 권한 부여 및 줄바꿈 변환
RUN chmod +x /app/gradlew

# 줄바꿈을 Unix 형식으로 변환 (sed 사용)
RUN sed -i 's/\r$//' /app/gradlew

# 6. JAR 빌드
RUN /bin/bash /app/gradlew bootJar

# 7. 최종 실행 스테이지
FROM openjdk:17-jdk-slim

# 8. 작업 디렉토리 설정
WORKDIR /app

# 9. 환경 변수 설정을 위한 설정 복사
COPY src/main/resources/application.yml /app/config/

# 10. 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar /app/dino.jar

# 11. 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=prod

# 12. JAR 파일을 실행하는 명령어 설정
ENTRYPOINT ["java", "-jar", "/app/dino.jar"]

# 13. 포트 노출
EXPOSE 8080
