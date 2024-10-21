# 빌드 -> jar 파일 생성
FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./ 
COPY gradle ./gradle
COPY config ./
COPY . .

RUN ./gradlew clean build

# jar 파일 실행
FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/PAPERPLE-BE-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
