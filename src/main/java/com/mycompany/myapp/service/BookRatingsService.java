package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BookRatings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing BookRatings.
 */
public interface BookRatingsService {

    /**
     * Save a bookRatings.
     *
     * @param bookRatings the entity to save
     * @return the persisted entity
     */
    BookRatings save(BookRatings bookRatings);

    /**
     *  Get all the bookRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookRatings> findAll(Pageable pageable);

    /**
     *  Get the "id" bookRatings.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookRatings findOne(String id);

    /**
     *  Delete the "id" bookRatings.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
