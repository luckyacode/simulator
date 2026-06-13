FROM eclipse-temurin:21-jre-alpine
LABEL authors="PRAVEEN"
ADD target/simulator-1.0.0-SNAPSHOT.jar simulator-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","simulator-1.0.0-SNAPSHOT.jar"]