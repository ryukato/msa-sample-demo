package com.msa.cqrs.queryservice.config;

import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {
    @Bean
    SpringAMQPMessageSource springAMQPMessageSource(Serializer axonJacksonSerializer) {
        return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(axonJacksonSerializer)) {
            @Override
            @RabbitListener(queues = "product.default.queue")
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }

    @Autowired
    public void configure(EventHandlingConfiguration configuration, SpringAMQPMessageSource springAMQPMessageSource) {
        configuration.registerSubscribingEventProcessor("accountEvents", c  -> springAMQPMessageSource);
    }

}
