package com.msa.cqrs.queryservice.handler;

import com.msa.cqrs.common.events.account.AccountCreatedEvent;
import com.msa.cqrs.queryservice.entity.AccountEntity;
import com.msa.cqrs.queryservice.repo.AccountRepository;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("accountEvents")
public class AccountEventHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountRepository repository;

    public AccountEventHandler(AccountRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        repository.save(new AccountEntity(event.getId(), event.getBalance(), event.getAccountCreator()));
    }

}
