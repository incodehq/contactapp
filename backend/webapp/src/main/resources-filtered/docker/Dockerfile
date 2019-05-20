FROM tomcat:8.0-jre8
MAINTAINER Rosco Kalis <roscokalis@gmail.com>
LABEL Vendor="Eurocommercial"

# COPY backend/webapp/target/contacts.war /usr/local/tomcat/webapps/contacts.war
# RUN cd /usr/local/tomcat/webapps/ && rm -rf ROOT && unzip contacts.war -d ROOT && rm contacts.war

EXPOSE 8080/tcp

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY ${docker-plugin.resource.include} /usr/local/tomcat/webapps/

COPY entrypoint.sh /root/

#ENTRYPOINT ["/root/entrypoint.sh"]
ENTRYPOINT ["sh", "/root/entrypoint.sh"]

