package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookRatings;

import com.mycompany.myapp.domain.Borrow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the BookRatings entity.
 */
@SuppressWarnings("unused")
public interface BookRatingsRepository extends MongoRepository<BookRatings,String> {

    @Query("{ 'isbn' : ?0 }")
    List<BookRatings> findBookRatingsByIsbn(String isbn);
}
