package com.msa.cqrs.commandservice.command.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final double amount;

    public DepositMoneyCommand(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "DepositMoneyCommand{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }
}
