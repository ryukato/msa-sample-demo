package com.msa.cqrs.commandservice.command.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class WithdrawMoneyCommand {

    @TargetAggregateIdentifier
    private final String id;
    private final double amount;

    public WithdrawMoneyCommand(String id, double amount) {
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
        return "WithdrawMoneyCommand{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }
}
