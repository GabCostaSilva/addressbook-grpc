
# Pull image.
FROM alpine:3.7

# Instalação do java, maven, wget e glibc.
RUN \
  apk add openjdk8 && \
  apk add maven && \
  apk --no-cache add ca-certificates wget && \
  wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && \
  wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.28-r0/glibc-2.28-r0.apk && \
  apk add glibc-2.28-r0.apk

# Define variável de ambiente JAVA_HOME 
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk

# Define variável de ambiente MAVEN_HOME 
ENV MAVEN_HOME /opt/maven

COPY . /var/www/
WORKDIR /var/www/

VOLUME /var/www/
EXPOSE 8080 443
