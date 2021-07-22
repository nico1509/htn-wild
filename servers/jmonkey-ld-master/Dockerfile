FROM maven:3-jdk-8 AS build
COPY . /jmonkey
RUN cd /jmonkey && mvn clean package


FROM tomcat:10-jdk8
COPY --from=build /jmonkey/target/jmonkey-ld-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps.dist/jmonkey.war
