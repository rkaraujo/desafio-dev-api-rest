FROM adoptopenjdk/openjdk11:alpine-jre
RUN mkdir /opt/app
COPY ./target/account-manager-1.0.0-SNAPSHOT.jar /opt/app/account-manager-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-Dapp.db.host=renato-postgres","-jar","/opt/app/account-manager-1.0.0-SNAPSHOT.jar"]
