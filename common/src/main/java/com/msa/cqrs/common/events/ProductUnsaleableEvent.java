package com.msa.cqrs.common.events;

public class ProductUnsaleableEvent extends AbstractEvent {
    public ProductUnsaleableEvent(String id) {
        super(id);
    }
}
