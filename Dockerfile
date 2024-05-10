# Use a base image with Java
FROM adoptopenjdk/openjdk17:alpine

# Set the working directory in the image
WORKDIR /app

# Copy the JAR file from the Maven build target directory into the image
COPY target/*.jar app.jar

# Specify the command to run your application when the container starts
CMD ["java", "-jar", "app.jar"]