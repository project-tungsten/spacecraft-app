package org.tungsten.service.hastelloy;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableAutoConfiguration
public class AppConfiguration {

    @Bean
    public EventBus eventBus(EventStorageEngine storageEngine) {
        return new EmbeddedEventStore(storageEngine);
    }

    @Bean
    @ConditionalOnProperty(name = "axon.eventstoreengine", havingValue = "mongodb")
    public EventStorageEngine eventStorageEngine(@Value("${mongodb.database-name}") final String databaseName, final MongoClient mongoClient) {
        final MongoTemplate mongoTemplate = new DefaultMongoTemplate(mongoClient, databaseName, "domainevents", "snapshotevents");
        return new MongoEventStorageEngine(mongoTemplate);
    }

    @Bean(name = "eventStorageEngine")
    @ConditionalOnProperty(name = "axon.eventstoreengine", havingValue = "inmemory", matchIfMissing = true)
    public EventStorageEngine inMemoryEventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public MongoClient mongoClient(@Value("${mongodb.uri}") final String mongoUri) {
        return new MongoClient(new MongoClientURI(mongoUri));
    }

    @Bean
    public Exchange exchange(AmqpAdmin admin, @Value("${axon.amqp.exchange}") final String exchangeName) {
        Exchange exchange = ExchangeBuilder.topicExchange(exchangeName).build();
        admin.declareExchange(exchange);
        return exchange;
    }

//    @Bean
//    public SpringAMQPPublisher springAMQPPublisher(EventStore eventStore) {
//        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore);
//        springAMQPPublisher.start(); // thought this will be implicit but not. So required to start before returning
//        return springAMQPPublisher;
//    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory(@Value("${rabbitmq.broker-url}") final String brokerUrl) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(brokerUrl);
        return connectionFactory;
    }
}

