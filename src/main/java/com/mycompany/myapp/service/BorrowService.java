package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Borrow.
 */
public interface BorrowService {

    /**
     * Save a borrow.
     *
     * @param borrow the entity to save
     * @return the persisted entity
     */
    Borrow save(Borrow borrow);

    /**
     *  Get all the borrows.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Borrow> findAll(Pageable pageable);

    /**
     *  Get the "id" borrow.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Borrow findOne(String id);

    /**
     *  Delete the "id" borrow.
     *
     *  @param id the id of the entity
     */
    void delete(String id);

    List<Borrow> findByUserId(String userId);
}
