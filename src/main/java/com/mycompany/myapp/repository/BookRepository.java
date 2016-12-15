package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Book;

import com.mycompany.myapp.domain.BookRatings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Book entity.
 */
@SuppressWarnings("unused")
public interface BookRepository extends MongoRepository<Book,String> {

    @Query("{ 'isbn' : ?0 }")
    List<Book> findBookByIsbn(String isbn);
}
