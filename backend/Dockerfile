FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /workspace/backend
COPY backend/pom.xml ./
COPY backend/src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/backend/target/team-task-manager.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
