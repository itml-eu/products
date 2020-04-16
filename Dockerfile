FROM openjdk:8-jdk-alpine
MAINTAINER ch.roche
VOLUME /tmp
EXPOSE 8080
ADD target/products-0.0.1-SNAPSHOT.jar products.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/products.jar"]