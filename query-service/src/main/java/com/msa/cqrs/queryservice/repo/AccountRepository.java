package com.msa.cqrs.queryservice.repo;

import com.msa.cqrs.queryservice.entity.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<AccountEntity, String> {

}
