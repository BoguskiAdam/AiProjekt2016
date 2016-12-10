package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BookContentService;
import com.mycompany.myapp.domain.BookContent;
import com.mycompany.myapp.repository.BookContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing BookContent.
 */
@Service
public class BookContentServiceImpl implements BookContentService{

    private final Logger log = LoggerFactory.getLogger(BookContentServiceImpl.class);
    
    @Inject
    private BookContentRepository bookContentRepository;

    /**
     * Save a bookContent.
     *
     * @param bookContent the entity to save
     * @return the persisted entity
     */
    public BookContent save(BookContent bookContent) {
        log.debug("Request to save BookContent : {}", bookContent);
        BookContent result = bookContentRepository.save(bookContent);
        return result;
    }

    /**
     *  Get all the bookContents.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<BookContent> findAll(Pageable pageable) {
        log.debug("Request to get all BookContents");
        Page<BookContent> result = bookContentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bookContent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public BookContent findOne(String id) {
        log.debug("Request to get BookContent : {}", id);
        BookContent bookContent = bookContentRepository.findOne(id);
        return bookContent;
    }

    /**
     *  Delete the  bookContent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete BookContent : {}", id);
        bookContentRepository.delete(id);
    }
}
