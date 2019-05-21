#FROM tomcat:8.0-jre8
#MAINTAINER Rosco Kalis <roscokalis@gmail.com>
#LABEL Vendor="Eurocommercial"

# COPY backend/webapp/target/contacts.war /usr/local/tomcat/webapps/contacts.war
# RUN cd /usr/local/tomcat/webapps/ && rm -rf ROOT && unzip contacts.war -d ROOT && rm contacts.war

#EXPOSE 8080

#RUN rm -rf /usr/local/tomcat/webapps/ROOT
#COPY ${docker-plugin.resource.include} /usr/local/tomcat/webapps/

#COPY entrypoint.sh /root/

#ENTRYPOINT ["/root/entrypoint.sh"]
#ENTRYPOINT ["sh", "/root/entrypoint.sh"]

#CMD /root/entrypoint.sh




FROM incodehq/tomcat:1.7.2
ADD ${docker-plugin.resource.include} ${DEPLOYMENT_DIR}/ROOT.war
ADD entrypoint.sh /
RUN chmod 755 /entrypoint.sh
CMD /entrypoint.sh
EXPOSE 8080
