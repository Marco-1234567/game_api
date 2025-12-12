FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY mvnw ./
COPY .mvn/ .mvn/
COPY pom.xml ./

# The \ is a line continuation character in Dockerfiles (and shell scripts). It allows you to break a long command across multiple lines for better readability.
# Without the \, Docker would treat them as separate commands, which is not what you want here. (extra layers!)
RUN chmod +x mvnw && \
  ./mvnw dependency:go-offline -B

COPY src/ ./src/

RUN ./mvnw clean package -DskipTests

# --------- runtime stage -------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
