package com.yousseefben;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

import java.util.Properties;

public class CustomKafkaProducer {

    private static final Logger log = Logger.getLogger(CustomKafkaProducer.class);

    private final String topic;
    private final KafkaProducer<String, String> producer;


    public CustomKafkaProducer(String topic, Properties props) {
        log.info("init producer");
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        this.topic = topic;
        producer = new KafkaProducer<>(props);

    }

    public void publishEvent(String value) {
        log.info("publish event");
        ProducerRecord<String, String> eventRecord =
                new ProducerRecord<>(topic, value);
        producer.send(eventRecord);
    }


}
