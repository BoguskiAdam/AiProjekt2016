package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BookContent;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the BookContent entity.
 */
@SuppressWarnings("unused")
public interface BookContentRepository extends MongoRepository<BookContent,String> {

    @Query("{ 'isbn' : ?0 }")
    List<BookContent> findBookContentByIsbn(String isbn);
}
