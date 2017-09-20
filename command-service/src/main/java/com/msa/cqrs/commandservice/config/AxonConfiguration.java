package com.msa.cqrs.commandservice.config;

import com.mongodb.MongoClient;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.documentpercommit.DocumentPerCommitStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AxonConfiguration {

    private static final String AMQP_CONFIG_KEY = "AMQP.Config";

    @Autowired
    MongoClient mongo;

    @Autowired
    public ConnectionFactory connectionFactory;

    @Value("${spring.application.queue}")
    private String queueName;

    @Value("${spring.application.exchange}")
    private String exchangeName;

    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Value("${spring.application.eventsCollectionName}")
    private String eventsCollectionName;

    @Value("${spring.application.snapshotCollectionName}")
    private String snapshotCollectionName;

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(
                mongo,
                databaseName,
                eventsCollectionName,
                snapshotCollectionName
        );
        return template;
    }

    @Bean
    EventStorageEngine eventStorageEngine(
            Serializer axonJacksonSerializer,
            MongoTemplate axonMongoTemplate) {
        MongoEventStorageEngine mongoEventStorageEngine =
                new MongoEventStorageEngine(
                        axonJacksonSerializer,
                        null,
                        axonMongoTemplate,
                        new DocumentPerCommitStorageStrategy()
                );

        return mongoEventStorageEngine;
    }



    // do not required since database is mongo. but when jpa is being used, then below codes are required.
//    @Bean
//    public TransactionManager springTransactionManager(PlatformTransactionManager platformTransactionManager) {
//        return new SpringTransactionManager(platformTransactionManager);
//    }

//    @Bean
//    public CommandBus commandBus() {
//        SimpleCommandBus commandBus = new SimpleCommandBus();
//        return commandBus;
//    }
//
}
