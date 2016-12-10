package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.BookContent;
import com.mycompany.myapp.service.BookContentService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BookContent.
 */
@RestController
@RequestMapping("/api")
public class BookContentResource {

    private final Logger log = LoggerFactory.getLogger(BookContentResource.class);
        
    @Inject
    private BookContentService bookContentService;

    /**
     * POST  /book-contents : Create a new bookContent.
     *
     * @param bookContent the bookContent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookContent, or with status 400 (Bad Request) if the bookContent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-contents")
    @Timed
    public ResponseEntity<BookContent> createBookContent(@Valid @RequestBody BookContent bookContent) throws URISyntaxException {
        log.debug("REST request to save BookContent : {}", bookContent);
        if (bookContent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookContent", "idexists", "A new bookContent cannot already have an ID")).body(null);
        }
        BookContent result = bookContentService.save(bookContent);
        return ResponseEntity.created(new URI("/api/book-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookContent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-contents : Updates an existing bookContent.
     *
     * @param bookContent the bookContent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookContent,
     * or with status 400 (Bad Request) if the bookContent is not valid,
     * or with status 500 (Internal Server Error) if the bookContent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-contents")
    @Timed
    public ResponseEntity<BookContent> updateBookContent(@Valid @RequestBody BookContent bookContent) throws URISyntaxException {
        log.debug("REST request to update BookContent : {}", bookContent);
        if (bookContent.getId() == null) {
            return createBookContent(bookContent);
        }
        BookContent result = bookContentService.save(bookContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookContent", bookContent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /book-contents : get all the bookContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookContents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/book-contents")
    @Timed
    public ResponseEntity<List<BookContent>> getAllBookContents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BookContents");
        Page<BookContent> page = bookContentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/book-contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /book-contents/:id : get the "id" bookContent.
     *
     * @param id the id of the bookContent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookContent, or with status 404 (Not Found)
     */
    @GetMapping("/book-contents/{id}")
    @Timed
    public ResponseEntity<BookContent> getBookContent(@PathVariable String id) {
        log.debug("REST request to get BookContent : {}", id);
        BookContent bookContent = bookContentService.findOne(id);
        return Optional.ofNullable(bookContent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /book-contents/:id : delete the "id" bookContent.
     *
     * @param id the id of the bookContent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookContent(@PathVariable String id) {
        log.debug("REST request to delete BookContent : {}", id);
        bookContentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookContent", id.toString())).build();
    }

}
