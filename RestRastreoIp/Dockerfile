# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/RestRastreoIp.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/RestRastreoIp.jar /opt/app/restrastreoip.jar
COPY ${JAR_FILE} restrastreoip.jar

EXPOSE 8080
# java -jar /opt/app/restrastreoip.jar
ENTRYPOINT ["java","-jar","restrastreoip.jar"]