FROM tomcat:9-jdk8-openjdk-slim

ENV DB_SERVER=localhost \
    DB_USERNAME=admin \
    DB_PASSWORD=password
WORKDIR /usr/local/tomcat/webapps/
COPY setenv.sh /usr/local/tomcat/bin/setenv.sh
RUN chmod ugo+x /usr/local/tomcat/bin/setenv.sh
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war
ENTRYPOINT ["catalina.sh", "run"]
