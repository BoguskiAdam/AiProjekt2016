package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BookRatingsService;
import com.mycompany.myapp.domain.BookRatings;
import com.mycompany.myapp.repository.BookRatingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing BookRatings.
 */
@Service
public class BookRatingsServiceImpl implements BookRatingsService{

    private final Logger log = LoggerFactory.getLogger(BookRatingsServiceImpl.class);
    
    @Inject
    private BookRatingsRepository bookRatingsRepository;

    /**
     * Save a bookRatings.
     *
     * @param bookRatings the entity to save
     * @return the persisted entity
     */
    public BookRatings save(BookRatings bookRatings) {
        log.debug("Request to save BookRatings : {}", bookRatings);
        BookRatings result = bookRatingsRepository.save(bookRatings);
        return result;
    }

    /**
     *  Get all the bookRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<BookRatings> findAll(Pageable pageable) {
        log.debug("Request to get all BookRatings");
        Page<BookRatings> result = bookRatingsRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bookRatings by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public BookRatings findOne(String id) {
        log.debug("Request to get BookRatings : {}", id);
        BookRatings bookRatings = bookRatingsRepository.findOne(id);
        return bookRatings;
    }

    /**
     *  Delete the  bookRatings by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete BookRatings : {}", id);
        bookRatingsRepository.delete(id);
    }
}
