package com.msa.cqrs.common.events.account;

import com.msa.cqrs.common.events.AbstractEvent;

public class MoneyDepositedEvent extends AbstractEvent {
    private final double amount;

    public MoneyDepositedEvent(String id, double amount) {
        super(id);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
