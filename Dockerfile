# Use the official Tomcat base image
FROM tomcat:9.0

# Copy the war file to the webapps directory
COPY target/Univ-db.war /usr/local/tomcat/webapps/

# Expose the port
EXPOSE 8080

