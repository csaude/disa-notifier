# Set the base image to use
FROM eclipse-temurin:17-jre-alpine

RUN ln -s /usr/share/zoneinfo/Africa/Maputo /etc/localtime

# Set the working directory
WORKDIR /opt/app

# Copy the JAR file to the container
COPY target/disa-notifier-*.jar /opt/app/disa-notifier.jar

# Expose the port that your application is running on
EXPOSE 8571

# Start the application
CMD ["java", "-jar", "disa-notifier.jar"]
#CMD ["java", "-agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n", "-jar", "disa-notifier-1.0.4-SNAPSHOT.jar"]

