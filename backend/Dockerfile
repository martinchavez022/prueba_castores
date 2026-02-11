# Build stage (has JDK + Maven)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Maven wrapper and pom first
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw

# Copy source code
COPY src src

# Build the application (downloads dependencies + compiles + packages)
RUN ./mvnw clean package -DskipTests

# Runtime stage (only JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]