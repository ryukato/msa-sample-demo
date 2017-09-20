package com.msa.cqrs.commandservice.command.account;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CloseAccountCommand {
    @TargetAggregateIdentifier
    private final String id;

    public CloseAccountCommand(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CloseAccountCommand{" +
                "id='" + id + '\'' +
                '}';
    }
}
