package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.BookRatings;
import com.mycompany.myapp.service.BookRatingsService;
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
 * REST controller for managing BookRatings.
 */
@RestController
@RequestMapping("/api")
public class BookRatingsResource {

    private final Logger log = LoggerFactory.getLogger(BookRatingsResource.class);

    @Inject
    private BookRatingsService bookRatingsService;

    /**
     * POST  /book-ratings : Create a new bookRatings.
     *
     * @param bookRatings the bookRatings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookRatings, or with status 400 (Bad Request) if the bookRatings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-ratings")
    @Timed
    public ResponseEntity<BookRatings> createBookRatings(@Valid @RequestBody BookRatings bookRatings) throws URISyntaxException {
        log.debug("REST request to save BookRatings : {}", bookRatings);
        if (bookRatings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookRatings", "idexists", "A new bookRatings cannot already have an ID")).body(null);
        }
        BookRatings result = bookRatingsService.save(bookRatings);
        return ResponseEntity.created(new URI("/api/book-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookRatings", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-ratings : Updates an existing bookRatings.
     *
     * @param bookRatings the bookRatings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookRatings,
     * or with status 400 (Bad Request) if the bookRatings is not valid,
     * or with status 500 (Internal Server Error) if the bookRatings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-ratings")
    @Timed
    public ResponseEntity<BookRatings> updateBookRatings(@Valid @RequestBody BookRatings bookRatings) throws URISyntaxException {
        log.debug("REST request to update BookRatings : {}", bookRatings);
        if (bookRatings.getId() == null) {
            return createBookRatings(bookRatings);
        }
        BookRatings result = bookRatingsService.save(bookRatings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookRatings", bookRatings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /book-ratings : get all the bookRatings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookRatings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/book-ratings")
    @Timed
    public ResponseEntity<List<BookRatings>> getAllBookRatings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BookRatings");
        Page<BookRatings> page = bookRatingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/book-ratings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /book-ratings/:id : get the "id" bookRatings.
     *
     * @param id the id of the bookRatings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookRatings, or with status 404 (Not Found)
     */
    @GetMapping("/book-ratings/{id}")
    @Timed
    public ResponseEntity<BookRatings> getBookRatings(@PathVariable String id) {
        log.debug("REST request to get BookRatings : {}", id);
        BookRatings bookRatings = bookRatingsService.findOne(id);
        return Optional.ofNullable(bookRatings)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/book-ratings/user/{userId}")
    @Timed
    public ResponseEntity<List<BookRatings>> getAllUserBorrows(@PathVariable String userId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Borrows");
        List<BookRatings> page = bookRatingsService.findByIsbnString(userId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    /**
     * DELETE  /book-ratings/:id : delete the "id" bookRatings.
     *
     * @param id the id of the bookRatings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookRatings(@PathVariable String id) {
        log.debug("REST request to delete BookRatings : {}", id);
        bookRatingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookRatings", id.toString())).build();
    }

}
