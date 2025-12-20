# ---------- 빌드 ----------
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# 캐시 활용 (의존성 먼저)
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon || true

COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# ---------- 실행 ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update && apt-get install -y fonts-nanum && \
    fc-cache -fv && \
    rm -rf /var/lib/apt/lists/*

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

# Spring Boot 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
