package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BookContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing BookContent.
 */
public interface BookContentService {

    public List<BookContent> findBookContentByIsbn (String isbn);
    /**
     * Save a bookContent.
     *
     * @param bookContent the entity to save
     * @return the persisted entity
     */
    BookContent save(BookContent bookContent);

    /**
     *  Get all the bookContents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookContent> findAll(Pageable pageable);

    /**
     *  Get the "id" bookContent.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookContent findOne(String id);

    /**
     *  Delete the "id" bookContent.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
