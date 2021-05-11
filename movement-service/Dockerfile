FROM maven:3-amazoncorretto-11

MAINTAINER Michel Olvera <michel.olvera.p@outlook.com>

COPY ./ /to_build/

WORKDIR /to_build/

RUN mvn clean compile package -DskipTests

RUN cp ./target/movement-service-0.0.1-SNAPSHOT.jar /movement-service-0.0.1-SNAPSHOT.jar

WORKDIR /

RUN rm -r /to_build/

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/movement-service-0.0.1-SNAPSHOT.jar"]