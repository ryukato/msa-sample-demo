package com.msa.cqrs.common.events.account;

import com.msa.cqrs.common.events.AbstractEvent;

public class MoneyWithdrawnEvent extends AbstractEvent {
    private final double amount;

    public MoneyWithdrawnEvent(String id, double amount) {
        super(id);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
