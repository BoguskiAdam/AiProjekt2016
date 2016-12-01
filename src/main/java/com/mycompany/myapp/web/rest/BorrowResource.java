package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Borrow;

import com.mycompany.myapp.repository.BorrowRepository;
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
 * REST controller for managing Borrow.
 */
@RestController
@RequestMapping("/api")
public class BorrowResource {

    private final Logger log = LoggerFactory.getLogger(BorrowResource.class);
        
    @Inject
    private BorrowRepository borrowRepository;

    /**
     * POST  /borrows : Create a new borrow.
     *
     * @param borrow the borrow to create
     * @return the ResponseEntity with status 201 (Created) and with body the new borrow, or with status 400 (Bad Request) if the borrow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/borrows")
    @Timed
    public ResponseEntity<Borrow> createBorrow(@Valid @RequestBody Borrow borrow) throws URISyntaxException {
        log.debug("REST request to save Borrow : {}", borrow);
        if (borrow.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("borrow", "idexists", "A new borrow cannot already have an ID")).body(null);
        }
        Borrow result = borrowRepository.save(borrow);
        return ResponseEntity.created(new URI("/api/borrows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("borrow", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /borrows : Updates an existing borrow.
     *
     * @param borrow the borrow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated borrow,
     * or with status 400 (Bad Request) if the borrow is not valid,
     * or with status 500 (Internal Server Error) if the borrow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/borrows")
    @Timed
    public ResponseEntity<Borrow> updateBorrow(@Valid @RequestBody Borrow borrow) throws URISyntaxException {
        log.debug("REST request to update Borrow : {}", borrow);
        if (borrow.getId() == null) {
            return createBorrow(borrow);
        }
        Borrow result = borrowRepository.save(borrow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("borrow", borrow.getId().toString()))
            .body(result);
    }

    /**
     * GET  /borrows : get all the borrows.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of borrows in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/borrows")
    @Timed
    public ResponseEntity<List<Borrow>> getAllBorrows(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Borrows");
        Page<Borrow> page = borrowRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/borrows");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /borrows/:id : get the "id" borrow.
     *
     * @param id the id of the borrow to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the borrow, or with status 404 (Not Found)
     */
    @GetMapping("/borrows/{id}")
    @Timed
    public ResponseEntity<Borrow> getBorrow(@PathVariable String id) {
        log.debug("REST request to get Borrow : {}", id);
        Borrow borrow = borrowRepository.findOne(id);
        return Optional.ofNullable(borrow)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /borrows/:id : delete the "id" borrow.
     *
     * @param id the id of the borrow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/borrows/{id}")
    @Timed
    public ResponseEntity<Void> deleteBorrow(@PathVariable String id) {
        log.debug("REST request to delete Borrow : {}", id);
        borrowRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("borrow", id.toString())).build();
    }

}
