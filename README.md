## Compile
`mvn clean install`
## Run server
`mvn clean install exec:java -Dexec.mainClass=com.usersbook.UsersBookServer`
## Run client
`mvn clean package exec:java -Dexec.mainClass=com.usersbook.UsersBookClient`

## RUN client without errors Threads
`mvn clean package exec:java -Dexec.mainClass=com.usersbook.UsersBookClient -Dexec.cleanupDaemonThreads=false`

## BUILD IMAGE docker
`docker build -t grpc .`
## RUN docker Windows
`docker run -it -p 8080:8080 -v "%cd%"/:/var/www/ grpc`
## RUN docker LINUX
`docker run -it -p 8080:8080 -v "$PWD"/:/var/www/ grpc`