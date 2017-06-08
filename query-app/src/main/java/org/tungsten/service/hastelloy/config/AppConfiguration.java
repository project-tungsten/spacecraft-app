package org.tungsten.service.hastelloy.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public MongoClient mongoClient(@Value("${mongodb.uri}") final String mongoUri) {
        return new MongoClient(new MongoClientURI(mongoUri));
    }

    @Bean
    public Exchange exchange(@Value("${axon.amqp.exchange}") final String exchangeName) {
        final Exchange exchange = ExchangeBuilder.topicExchange(exchangeName).build();
        amqpAdmin.declareExchange(exchange);
        return exchange;
    }

    @Bean
    public Queue queue(@Value("${rabbitmq.queue-name}") final String queueName) {
        final Queue queue = QueueBuilder.durable(queueName).build();
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue, @Value("${rabbitmq.queue.routing-key:#}") final String routingKey) {
        final Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
        amqpAdmin.declareBinding(binding);
        return binding;
    }

    @Autowired
    public void configure(EventHandlingConfiguration ehConfig, SpringAMQPMessageSource springAMQPMessageSource) {
        ehConfig.registerSubscribingEventProcessor("amqpProcessor", c -> springAMQPMessageSource);
        ehConfig.byDefaultAssignTo("amqpProcessor");
    }

    @Bean
    public SpringAMQPMessageSource springAMQPMessageSource(final Serializer serializer) {
        mapEventAlias((XStreamSerializer) serializer);
        return new SpringAMQPMessageSource(serializer);
    }

    @Bean
    public AppMessageListenerAdapter appMessageListenerAdapter(final SpringAMQPMessageSource springAMQPMessageSource){
        return new AppMessageListenerAdapter(springAMQPMessageSource);
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(final ConnectionFactory connectionFactory,
                                                             @Value("${rabbitmq.queue-name}") final String amqpQueueName,
                                                             final AppMessageListenerAdapter messageListener) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(amqpQueueName);
        container.setMessageListener(messageListener);
        return container;
    }

    private void mapEventAlias(final XStreamSerializer serializer) {
        serializer.addAlias("org.tungsten.service.hastelloy.event.SpaceCraftCreatedEvent", SpaceCraftCreatedEvent.class);
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory(@Value("${rabbitmq.broker-url}") final String brokerUrl) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(brokerUrl);
        return connectionFactory;
    }
}

