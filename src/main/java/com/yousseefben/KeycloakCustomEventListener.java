package com.yousseefben;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.util.Properties;

public class KeycloakCustomEventListener implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(KeycloakCustomEventListener.class);


    private final CustomKafkaProducer customKafkaProducer;
    private ObjectMapper mapper;

    public KeycloakCustomEventListener(String topicKafka, Properties props) {
        log.info("init custom event listener");
        mapper = new ObjectMapper();
        customKafkaProducer = new CustomKafkaProducer(topicKafka, props);
    }

    @Override
    public void onEvent(Event event) {
        log.info("Event: " + event.getType() + "userId: {}" + event.getUserId());
        try {
            customKafkaProducer.publishEvent(mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("error: " + e.getMessage());
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.info("Admin Event: " + adminEvent.getResourceType().name());
        try {
            customKafkaProducer.publishEvent(mapper.writeValueAsString(adminEvent));
        } catch (JsonProcessingException e) {
            log.error("error: " + e.getMessage());
        }
    }

    @Override
    public void close() {

    }
}
