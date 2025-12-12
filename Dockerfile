FROM eclipse-temurin:21.0.9_10-jre
WORKDIR /app
COPY build/libs/reservation-system-0.0.1-SNAPSHOT.jar /app/reservation.jar
ENTRYPOINT ["java", "-jar", "/app/reservation.jar"]
