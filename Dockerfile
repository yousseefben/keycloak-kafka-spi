FROM quay.io/keycloak/keycloak:15.0.2
ADD ./build/libs/keycloak-spi-1.0-SNAPSHOT.jar /opt/jboss/keycloak/standalone/deployments/

#ADD realm-export.json /init/

# to prevent 'java.nio.file.DirectoryNotEmptyException: /opt/jboss/keycloak/standalone/configuration/standalone_xml_history/current' on startup
RUN rm -rf /opt/jboss/keycloak/standalone/configuration/standalone_xml_history

# from base image
#USER 1000

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]

CMD ["-b", "0.0.0.0"]
