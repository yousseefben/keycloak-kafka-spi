package com.yousseefben;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.Properties;


public class KeycloakCustomEventListenerFactory implements EventListenerProviderFactory {
    private KeycloakCustomEventListener keycloakCustomEventListener;

    private static final Logger log = Logger.getLogger(KeycloakCustomEventListenerFactory.class);

    private String topicKafka;
    private String bootstrapServers;
    private boolean sslEnabled;
    private String keystoreLocation;
    private String keystorePassword;
    private String trustSoreLocation;
    private String trustSorePassword;
    private boolean jaasEnabled;
    private String jaasConfig;
    private String saslMechanism;
    private String securityProtocol;
    private Properties properties;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        if (keycloakCustomEventListener == null) {
            keycloakCustomEventListener = new KeycloakCustomEventListener(topicKafka, properties);
        }
        return keycloakCustomEventListener;
    }

    @Override
    public void init(Config.Scope scope) {
        log.info("Init kafka");


        bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        keystoreLocation = System.getenv("KAFKA_SSL_KEYSTORE_LOCATION");
        keystorePassword = System.getenv("KAFKA_SSL_KEYSTORE_PASSWORD");
        trustSoreLocation = System.getenv("KAFKA_SSL_TRUSTSTORE_LOCATION");
        trustSorePassword = System.getenv("KAFKA_SSL_TRUSTSTORE_PASSWORD");
        jaasConfig = System.getenv("KAFKA_SASL_JAAS_CONFIG");
        saslMechanism = System.getenv("KAFKA_DEFAULT_SASL_MECHANISM");
        securityProtocol = System.getenv("KAFKA_SECURITY_PROTOCOL");
        sslEnabled = "true".equalsIgnoreCase(System.getenv("KAFKA_SSL_ENABLED"));
        jaasEnabled = "true".equalsIgnoreCase(System.getenv("KAFKA_JAAS_ENABLED"));

        topicKafka = System.getenv("KAFKA_TOPIC");

        log.info("Kafka ssl enabled: " + sslEnabled);

        if (topicKafka == null || topicKafka.isEmpty()) {
            throw new NullPointerException("topic is required.");
        }
        if (bootstrapServers == null || bootstrapServers.isEmpty()) {
            throw new NullPointerException("bootstrapServers are required");
        }
        if (sslEnabled && (keystoreLocation == null || keystorePassword == null || trustSoreLocation == null || trustSorePassword == null)) {
            throw new NullPointerException("ssl params required");
        }
            properties = getProperties();

        }

        @Override
        public void postInit (KeycloakSessionFactory keycloakSessionFactory){

        }

        @Override
        public void close () {

        }

        @Override
        public String getId () {
            return "kafka-event";
        }

        private Properties getProperties () {
            Properties propsKafka = new Properties();
        propsKafka.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        propsKafka.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsKafka.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (sslEnabled) {
            propsKafka.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystoreLocation);
            propsKafka.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystorePassword);
            propsKafka.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustSoreLocation);
            propsKafka.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustSorePassword);
        }
        if (jaasEnabled) {
            propsKafka.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
            propsKafka.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
            propsKafka.put("security.protocol", securityProtocol);

        }
            return propsKafka;
        }
    }
