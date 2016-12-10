package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Borrow;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Borrow entity.
 */
@SuppressWarnings("unused")
public interface BorrowRepository extends MongoRepository<Borrow,String> {
    @Query("{ 'userId' : ?0 }")
    List<Borrow> findBorrowsByUserId(String userId);
}
