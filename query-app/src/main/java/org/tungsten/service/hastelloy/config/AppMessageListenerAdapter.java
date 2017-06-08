package org.tungsten.service.hastelloy.config;

import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

public class AppMessageListenerAdapter implements ChannelAwareMessageListener {
    private final SpringAMQPMessageSource springAMQPMessageSource;

    public AppMessageListenerAdapter(final SpringAMQPMessageSource springAMQPMessageSource) {
        this.springAMQPMessageSource = springAMQPMessageSource;
    }

    @Override
    public void onMessage(final Message message, final Channel channel) throws Exception {
        System.out.println("Received Message with routing-key: " + message.getMessageProperties().getReceivedRoutingKey());
        springAMQPMessageSource.onMessage(message, channel);
    }
}
