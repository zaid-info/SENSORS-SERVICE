FROM openjdk
VOLUME /tmp
ADD target/SENSORS-SERVICE*.jar /SENSORS-SERVICE.jar
CMD ["java","-jar","/SENSORS-SERVICE.jar","--spring.profiles.active=prod"]
EXPOSE 8084
