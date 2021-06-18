FROM ubuntu:latest
COPY target/TP-Final-0.0.1-SNAPSHOT.jar app/cliente.jar
EXPOSE 17171
RUN apt-get update && apt-get upgrade && apt-get install -y libxtst6 && apt-get install -y openjdk 
CMD ["java","-jar","app/cliente.jar"]

