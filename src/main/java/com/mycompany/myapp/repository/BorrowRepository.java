package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Borrow;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Borrow entity.
 */
@SuppressWarnings("unused")
public interface BorrowRepository extends MongoRepository<Borrow,String> {

}
