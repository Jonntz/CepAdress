FROM maven:3.9.9-amazoncorretto-21 as build
WORKDIR /app
COPY . .
RUN mvn clean package -X -DskipTests

FROM openjdk:21-ea-1-jdk-slim
WORKDIR /app
COPY --from=build ./app/target/*.jar ./cepAddress.jar
ENTRYPOINT java -jar cepAddress.jar