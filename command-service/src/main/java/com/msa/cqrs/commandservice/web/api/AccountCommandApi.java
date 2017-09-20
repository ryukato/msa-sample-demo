package com.msa.cqrs.commandservice.web.api;

import com.msa.cqrs.commandservice.aggregates.account.AccountOwner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RequestMapping(value = "/accounts")
public interface AccountCommandApi {
    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createAccount(@RequestBody AccountOwner user);

    @PostMapping(path = "{accountId}/balance")
    public CompletableFuture<ResponseEntity> deposit(@PathVariable("accountId") String accountId, @RequestBody double amount);

    @DeleteMapping(path = "{accountId}")
    public CompletableFuture<ResponseEntity> delete(@PathVariable("accountId") String accountId);
}
