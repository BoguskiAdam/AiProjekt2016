package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookRatings;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the BookRatings entity.
 */
@SuppressWarnings("unused")
public interface BookRatingsRepository extends MongoRepository<BookRatings,String> {
}
