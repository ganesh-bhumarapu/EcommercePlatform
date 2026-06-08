# Use a Maven image that already includes Eclipse Temurin JDK 17
FROM maven:3.9.8-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only pom first to leverage docker cache for dependencies
COPY ecommerce/pom.xml ./ecommerce/pom.xml
WORKDIR /app/ecommerce
RUN mvn -B -f pom.xml dependency:go-offline

# Copy rest of the sources and build
COPY . .
RUN mvn -B -f pom.xml clean package -DskipTests

# Runtime image: Java 17 JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/ecommerce/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]