FROM maven:3.9-eclipse-temurin-21
WORKDIR /app

# Create data directory for SQLite
RUN mkdir -p /app/data

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn compile

EXPOSE 8080
CMD ["mvn", "exec:java", "-Dexec.mainClass=nullscape.mike.Main"]