# Publish Keycloak Events to Kafka using SPI(plugins)

You can find more details in the Blog post: https://xxx

## Build jar
```
./gradlew build
```

## Build image Docker
```
docker build -t keycloak-kafka .
```

## Run Docker image
```
#you can add here the other environment variables
docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -e KAFKA_TOPIC=MY_TOPIC keycloak-kafka
```

## Deploy to Keycloak server

cp keycloak-kafka.jar /keycloak-x.x.x/standalone/deployments

