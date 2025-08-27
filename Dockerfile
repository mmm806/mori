# Dockerfile
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
# 소스 복사 후 빌드
COPY . .
RUN ./gradlew -q clean bootJar

FROM eclipse-temurin:17-jre
ENV TZ=Asia/Seoul
WORKDIR /app
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
