FROM adoptopenjdk/openjdk11

WORKDIR /app

RUN echo "Asia/Seoul" > /etc/timezone && \
    apt-get update && apt-get install -y tzdata && \
    rm /etc/localtime && \
    ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata

COPY . .
RUN ./mvnw clean package

CMD ["java", "-jar", "target/gal-ma-0.0.1-SNAPSHOT.jar"]