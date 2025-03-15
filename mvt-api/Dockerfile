FROM eclipse-temurin:17-jdk as build

WORKDIR /app

# Copy maven configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build dependencies
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime image
FROM tomcat:10-jre17

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Create directory for file uploads
RUN mkdir -p /usr/local/tomcat/uploads
VOLUME /usr/local/tomcat/uploads

# Copy the war from the build stage to Tomcat's webapps directory as ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV CATALINA_OPTS="-Dfile.upload-dir=/usr/local/tomcat/uploads"

EXPOSE 8080

CMD ["catalina.sh", "run"]