package com.msa.cqrs.queryservice.web.api.impl;

import com.msa.cqrs.queryservice.entity.AccountEntity;
import com.msa.cqrs.queryservice.entity.ProductEntity;
import com.msa.cqrs.queryservice.repo.AccountRepository;
import com.msa.cqrs.queryservice.web.api.AccountRestApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class AccountRestApiImpl implements AccountRestApi {
    private final AccountRepository accountRepository;

    public AccountRestApiImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public CompletableFuture<ResponseEntity<Page<AccountEntity>>> findAll(Pageable pageable) {
        try {
            return CompletableFuture.supplyAsync(() -> accountRepository.findAll(pageable)).thenApply(list -> ResponseEntity.ok(list));
        } catch (AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        }
    }

    @Override
    public CompletableFuture<ResponseEntity<AccountEntity>> findById(String id) {
        try {
            Assert.hasLength(id, "Missing product id");
            return CompletableFuture.supplyAsync(() -> {
                AccountEntity entity = accountRepository.findOne(id);
                if (Optional.ofNullable(entity).isPresent()) {
                    return ResponseEntity.ok(entity);
                } else {
                    return ResponseEntity.notFound().build();
                }
            });
        } catch (AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        }
    }
}
