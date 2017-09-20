package com.msa.cqrs.queryservice.repo;

import com.msa.cqrs.queryservice.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductEntityRepository extends MongoRepository<ProductEntity, String> {

}
