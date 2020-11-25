
# Pull base image.
FROM ubuntu:20.04

# Install Java.
RUN \
  apt-get update && \
  apt-get -y upgrade && \ 
  apt-get install -y openjdk-8-jdk && \
  apt-get install -y maven && \
  rm -rf /var/lib/apt/lists/*

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV MAVEN_HOME /opt/maven

COPY . /var/www/
WORKDIR /var/www/

VOLUME /var/www/
EXPOSE 8080 443

# ENTRYPOINT ["mvn", "clean", "install", "exec:java", "-Dexec.mainClass=com.addressbook.AddressBookServer"]
 
# Define default command.
# CMD ["clean", "install", "exec:java", "-Dexec.mainClass=com.addressbook.AddressBookServer"]