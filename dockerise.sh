#!/usr/bin/env sh

mvn clean install && docker build -t rkalis/contactapp . && docker run -dt rkalis/contactapp -p "isis.manifist=1231231;isis.database.driver=sqlServer;"
