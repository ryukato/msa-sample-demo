package com.msa.cqrs.common.events;

public class ProductSaleableEvent extends AbstractEvent {
    public ProductSaleableEvent(String id) {
        super(id);
    }
}
