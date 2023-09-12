package com.bingo.test.esAndMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author h-bingo
 * @Date 2023-09-08 16:32
 * @Version 1.0
 */
@Repository
public interface EsAndMongoModelRepository extends MongoRepository<EsAndMongoModel, String> {
}
