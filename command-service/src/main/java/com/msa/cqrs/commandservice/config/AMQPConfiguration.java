package com.msa.cqrs.commandservice.config;

import org.axonframework.boot.AMQPProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnExpression("'${spring.profiles.active}' != 'dev'")
@Configuration
public class AMQPConfiguration {
    @Value("${axon.amqp.queue")
    private String queueName;
    private final RabbitProperties rabbitProperties;
    private final AMQPProperties amqpProperties;

    public AMQPConfiguration(RabbitProperties rabbitProperties, AMQPProperties amqpProperties) {
        this.rabbitProperties = rabbitProperties;
        this.amqpProperties = amqpProperties;
    }

    @Bean
    Queue defaultStream() {
        return new Queue("product.default.queue", true);
    }

    @Bean
    FanoutExchange eventBusExchange() {
        return new FanoutExchange(amqpProperties.getExchange(), true, false);
    }


    @Bean
    Binding binding(Queue queue, FanoutExchange eventBusExchange) {
        return BindingBuilder.bind(queue).to(eventBusExchange);
//        return new Binding(
//                queueName,
//                Binding.DestinationType.QUEUE,
//                amqpProperties.getExchange(),
//                "*",
//                null);
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitProperties.getHost());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    @Required
    public RabbitAdmin rabbitAdmin(Queue queue, FanoutExchange eventBusExchange) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        admin.setAutoStartup(true);
        admin.declareExchange(eventBusExchange());
        admin.declareQueue(defaultStream());
        admin.declareBinding(binding(queue, eventBusExchange));
        admin.afterPropertiesSet();
        return admin;
    }

    @Bean
    RabbitTransactionManager transactionManager() {
        RabbitTransactionManager transactionManager = new RabbitTransactionManager(connectionFactory());
        return transactionManager;
    }
}
