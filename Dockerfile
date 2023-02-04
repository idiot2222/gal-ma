FROM adoptopenjdk/openjdk11

WORKDIR /app

COPY . .
RUN ./mvnw clean package

CMD ["java", "-jar", "target/gal-ma-0.0.1-SNAPSHOT.jar"]