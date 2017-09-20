package com.msa.cqrs.queryservice.web.api.impl;

import com.msa.cqrs.queryservice.entity.ProductEntity;
import com.msa.cqrs.queryservice.repo.ProductEntityRepository;
import com.msa.cqrs.queryservice.web.api.ProductRestApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class ProductRestApiImpl implements ProductRestApi {
    private final ProductEntityRepository productEntityRepository;

    public ProductRestApiImpl(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Override
    public CompletableFuture<ResponseEntity<ProductEntity>> findById(String id) {
        try {
            Assert.hasLength(id, "Missing product id");
            return CompletableFuture.supplyAsync(() -> {
                ProductEntity entity = productEntityRepository.findOne(id);
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

    @Override
    public CompletableFuture<ResponseEntity<Page<ProductEntity>>> findAll(final Pageable pageable) {
        try {
            return CompletableFuture.supplyAsync(() -> productEntityRepository.findAll(pageable)).thenApply(list -> ResponseEntity.ok(list));
        } catch (AssertionError | IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ResponseEntity(HttpStatus.BAD_REQUEST));
        }
    }
}
