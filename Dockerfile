
# Pull base image.
FROM alpine:3.7

# Install Java.
RUN \
  apk add openjdk8 && \
  apk add maven

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV MAVEN_HOME /opt/maven

COPY . /var/www/
WORKDIR /var/www/

VOLUME /var/www/
EXPOSE 8080 443

# ENTRYPOINT ["mvn", "clean", "install", "exec:java", "-Dexec.mainClass=com.addressbook.AddressBookServer"]
 
# Define default command.
# CMD ["clean", "install", "exec:java", "-Dexec.mainClass=com.addressbook.AddressBookServer"]