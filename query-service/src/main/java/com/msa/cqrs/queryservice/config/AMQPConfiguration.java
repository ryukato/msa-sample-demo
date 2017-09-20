package com.msa.cqrs.queryservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@ConditionalOnExpression("'${spring.profiles.active}' != 'dev'")
@Configuration
public class AMQPConfiguration {

    @Value("${spring.application.index}")
    private Integer index;

    @Value("${spring.rabbitmq.queue}")
    private String queueName = "product.default.queue";

    @Value("${axon.amqp.exchange}")
    private String exchangeName;

    private final RabbitProperties rabbitProperties;

    public AMQPConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    Queue eventStream() {
        // auto-delete: false (default)
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    @Bean
    Exchange eventBusExchange() {
        // auto-delete: false (default)
        return ExchangeBuilder
                .fanoutExchange(exchangeName)
                .durable(true)
                .build();
    }

    @Bean
    Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("*").noargs();
    }

    @Bean
    ConnectionFactory connectionFactory() throws Exception {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitProperties.getHost());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean
    @Required
    public AmqpAdmin rabbitAdmin(
            ConnectionFactory connectionFactory,
            Exchange exchange,
            Queue queue,
            Binding binding
    ) throws Exception {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
        admin.afterPropertiesSet();
        return admin;
    }

    @Bean
    RabbitTransactionManager transactionManager(ConnectionFactory connectionFactory) throws Exception {
        return new RabbitTransactionManager(connectionFactory);
    }
}