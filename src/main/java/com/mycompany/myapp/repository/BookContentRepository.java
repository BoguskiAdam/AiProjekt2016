package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookContent;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the BookContent entity.
 */
@SuppressWarnings("unused")
public interface BookContentRepository extends MongoRepository<BookContent,String> {

}
