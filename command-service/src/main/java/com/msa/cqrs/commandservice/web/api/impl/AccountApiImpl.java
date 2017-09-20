package com.msa.cqrs.commandservice.web.api.impl;

import com.msa.cqrs.commandservice.aggregates.account.AccountOwner;
import com.msa.cqrs.commandservice.aggregates.account.BankAccount;
import com.msa.cqrs.commandservice.command.account.CloseAccountCommand;
import com.msa.cqrs.commandservice.command.account.CreateAccountCommand;
import com.msa.cqrs.commandservice.command.account.DepositMoneyCommand;
import com.msa.cqrs.commandservice.command.account.WithdrawMoneyCommand;
import com.msa.cqrs.commandservice.web.api.AccountCommandApi;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class AccountApiImpl implements AccountCommandApi {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CommandGateway commandGateway;

    @Autowired
    public AccountApiImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<ResponseEntity<String>> createAccount(AccountOwner user) {
        try {
            Assert.hasLength(user.getName(), "Missing account creator");
            String accountId = UUID.randomUUID().toString();
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() ->
                commandGateway.sendAndWait(new CreateAccountCommand(accountId, user.getName()))
            );
            return completableFuture
                    .thenApply(s -> new ResponseEntity<>(s, HttpStatus.CREATED))
                    .exceptionally((e) -> {
                        logger.warn("Create Account {} failed", user);
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    });
        } catch(AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        } catch (CommandExecutionException cex) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.CONFLICT));
        }
    }

    @Override
    public CompletableFuture<ResponseEntity> deposit(String accountId, double amount) {
        try {
            CompletableFuture<ResponseEntity> completableFuture;
            if (amount > 0) {
                completableFuture = commandGateway.sendAndWait(new DepositMoneyCommand(accountId, amount));
            } else {
                completableFuture = commandGateway.sendAndWait(new WithdrawMoneyCommand(accountId, -amount));
            }
            return completableFuture.thenApply((s) -> new ResponseEntity(HttpStatus.OK));
        } catch (AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        } catch (CommandExecutionException cex) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.CONFLICT));
        }
    }

    @Override
    public CompletableFuture delete(String accountId) {
        try {
            return commandGateway.sendAndWait(new CloseAccountCommand(accountId));
        }catch (AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        } catch (CommandExecutionException cex) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.CONFLICT));
        }
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound(){}

    @ExceptionHandler(BankAccount.InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String insufficientBalance(BankAccount.InsufficientBalanceException e) {
        return e.getMessage();
    }
}
