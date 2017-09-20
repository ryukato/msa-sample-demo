package com.msa.cqrs.commandservice.command.account;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateAccountCommand {

    @TargetAggregateIdentifier
    private final String id;
    private final String accountCreator;

    public CreateAccountCommand(String id, String accountCreator) {
        this.id = id;
        this.accountCreator = accountCreator;
    }

    public String getId() {
        return id;
    }

    public String getAccountCreator() {
        return accountCreator;
    }

    @Override
    public String toString() {
        return "CreateAccountCommand{" +
                "id='" + id + '\'' +
                ", accountCreator='" + accountCreator + '\'' +
                '}';
    }
}
