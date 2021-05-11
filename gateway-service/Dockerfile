FROM amazoncorretto:11

MAINTAINER Michel Olvera <michel.olvera.p@outlook.com>

COPY ./ /to_build/

WORKDIR /to_build/

RUN ./mvnw clean compile package -DskipTests

RUN cp ./target/gateway-service-0.0.1-SNAPSHOT.jar /gateway-service-0.0.1-SNAPSHOT.jar

WORKDIR /

RUN rm -r /to_build/

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/gateway-service-0.0.1-SNAPSHOT.jar"]