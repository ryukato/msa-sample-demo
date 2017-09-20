package com.msa.cqrs.queryservice.handler;

import com.msa.cqrs.common.events.ProductAddedEvent;
import com.msa.cqrs.queryservice.entity.ProductEntity;
import com.msa.cqrs.queryservice.repo.ProductEntityRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class ProductEventHandler {
    private final ProductEntityRepository repository;

    public ProductEventHandler(ProductEntityRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ProductAddedEvent event) {
        repository.save(new ProductEntity(event.getId(), event.getName()));
    }
}
