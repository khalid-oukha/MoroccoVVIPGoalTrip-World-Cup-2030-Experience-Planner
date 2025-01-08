# Use Tomcat as the base image
FROM tomcat:10.1-jdk17

# Set the working directory
WORKDIR /usr/local/tomcat/webapps/

# Copy the WAR file to the Tomcat webapps directory
COPY target/*.war app.war

# Expose Tomcat's default port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
