package org.tungsten.service.hastelloy;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tungsten.service.hastelloy.query.event.SpaceCraftCreatedEvent;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableAutoConfiguration
public class AppConfiguration {

    @Bean
    public MongoClient mongoClient(@Value("${mongodb.uri}") final String mongoUri) {
        return new MongoClient(new MongoClientURI(mongoUri));
    }

    @Autowired
    public void configure(EventHandlingConfiguration ehConfig, SpringAMQPMessageSource springAMQPMessageSource) {
        ehConfig.registerSubscribingEventProcessor("amqpProcessor", c -> springAMQPMessageSource);
        ehConfig.byDefaultAssignTo("amqpProcessor");
    }

    @Bean
    public SpringAMQPMessageSource springAMQPMessageSource(@Value("${amqp.queue-name}") final String amqpQueueName, final Serializer serializer) {
        mapEventAlias((XStreamSerializer) serializer);
        return new SpringAMQPMessageSource(serializer) {
            @RabbitListener(queues = "test-domain-queue-1")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }

    private void mapEventAlias(final XStreamSerializer serializer) {
        serializer.addAlias("org.tungsten.service.hastelloy.event.SpaceCraftCreatedEvent", SpaceCraftCreatedEvent.class);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${rabbitmq.broker-url}") final String brokerUrl) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri(brokerUrl);
        return connectionFactory;
    }
}

