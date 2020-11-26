
# Pull base image.
FROM alpine:3.7

# Install Java.
RUN \
  apk add openjdk8 && \
  apk add maven && \
  apk --no-cache add ca-certificates wget && \
  wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
  wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.28-r0/glibc-2.28-r0.apk && \
  apk add glibc-2.28-r0.apk

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