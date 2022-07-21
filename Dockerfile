FROM openjdk:17
EXPOSE 8080 587
ADD target/login_Service.jar login_Service.jar
ENTRYPOINT ["java", "-jar", "/login_Service"]