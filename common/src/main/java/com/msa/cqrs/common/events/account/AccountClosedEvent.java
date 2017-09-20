package com.msa.cqrs.common.events.account;

import com.msa.cqrs.common.events.AbstractEvent;

public class AccountClosedEvent extends AbstractEvent {
    public AccountClosedEvent(String id) {
        super(id);
    }
}
