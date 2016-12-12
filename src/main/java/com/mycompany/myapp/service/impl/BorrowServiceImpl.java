package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BorrowService;
import com.mycompany.myapp.domain.Borrow;
import com.mycompany.myapp.repository.BorrowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Borrow.
 */
@Service
public class BorrowServiceImpl implements BorrowService{

    private final Logger log = LoggerFactory.getLogger(BorrowServiceImpl.class);

    @Inject
    private BorrowRepository borrowRepository;

    /**
     * Save a borrow.
     *
     * @param borrow the entity to save
     * @return the persisted entity
     */
    public Borrow save(Borrow borrow) {
        log.debug("Request to save Borrow : {}", borrow);
        Borrow result = borrowRepository.save(borrow);
        return result;
    }

    /**
     *  Get all the borrows.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Borrow> findAll(Pageable pageable) {
        log.debug("Request to get all Borrows");
        Page<Borrow> result = borrowRepository.findAll(pageable);
        return result;
    }
    public List<Borrow> findAll() {
        log.debug("Request to get all Borrows");
        List<Borrow> result = borrowRepository.findAll();
        return result;
    }

    public List<Borrow> findByUserId(String userId) {
        log.debug("Request to get all Borrows by user id");
        List<Borrow> result = borrowRepository.findBorrowsByUserId(userId);
        return result;
    }
    /**
     *  Get one borrow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Borrow findOne(String id) {
        log.debug("Request to get Borrow : {}", id);
        Borrow borrow = borrowRepository.findOne(id);
        return borrow;
    }

    /**
     *  Delete the  borrow by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Borrow : {}", id);
        borrowRepository.delete(id);
    }
}
