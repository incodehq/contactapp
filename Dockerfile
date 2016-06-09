FROM tomcat:8.0-jre8
MAINTAINER Rosco Kalis <roscokalis@gmail.com>
LABEL Vendor="Eurocommercial"

COPY backend/webapp/target/contacts.war /usr/local/tomcat/webapps
RUN cd /usr/local/tomcat/webapps/ && unzip contacts.war -d contacts && rm contacts.war
COPY entrypoint.sh /root/

ENTRYPOINT ["/root/entrypoint.sh"]
