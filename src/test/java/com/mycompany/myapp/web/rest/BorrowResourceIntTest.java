package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AiProjektApp;

import com.mycompany.myapp.domain.Borrow;
import com.mycompany.myapp.repository.BorrowRepository;
import com.mycompany.myapp.service.BorrowService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BorrowResource REST controller.
 *
 * @see BorrowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiProjektApp.class)
public class BorrowResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAA";
    private static final String UPDATED_USER_ID = "BBBBB";

    private static final String DEFAULT_ISBN = "AAAAAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_BORROW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_FEE = new BigDecimal(1);

    @Inject
    private BorrowRepository borrowRepository;

    @Inject
    private BorrowService borrowService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBorrowMockMvc;

    private Borrow borrow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BorrowResource borrowResource = new BorrowResource();
        ReflectionTestUtils.setField(borrowResource, "borrowService", borrowService);
        this.restBorrowMockMvc = MockMvcBuilders.standaloneSetup(borrowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Borrow createEntity() {
        Borrow borrow = new Borrow()
                .userId(DEFAULT_USER_ID)
                .isbn(DEFAULT_ISBN)
                .borrowDate(DEFAULT_BORROW_DATE)
                .returnDate(DEFAULT_RETURN_DATE)
                .fee(DEFAULT_FEE);
        return borrow;
    }

    @Before
    public void initTest() {
        borrowRepository.deleteAll();
        borrow = createEntity();
    }

    @Test
    public void createBorrow() throws Exception {
        int databaseSizeBeforeCreate = borrowRepository.findAll().size();

        // Create the Borrow

        restBorrowMockMvc.perform(post("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrow)))
                .andExpect(status().isCreated());

        // Validate the Borrow in the database
        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeCreate + 1);
        Borrow testBorrow = borrows.get(borrows.size() - 1);
        assertThat(testBorrow.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBorrow.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBorrow.getBorrowDate()).isEqualTo(DEFAULT_BORROW_DATE);
        assertThat(testBorrow.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBorrow.getFee()).isEqualTo(DEFAULT_FEE);
    }

    @Test
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = borrowRepository.findAll().size();
        // set the field null
        borrow.setUserId(null);

        // Create the Borrow, which fails.

        restBorrowMockMvc.perform(post("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrow)))
                .andExpect(status().isBadRequest());

        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkIsbnIsRequired() throws Exception {
        int databaseSizeBeforeTest = borrowRepository.findAll().size();
        // set the field null
        borrow.setIsbn(null);

        // Create the Borrow, which fails.

        restBorrowMockMvc.perform(post("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrow)))
                .andExpect(status().isBadRequest());

        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkBorrowDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = borrowRepository.findAll().size();
        // set the field null
        borrow.setBorrowDate(null);

        // Create the Borrow, which fails.

        restBorrowMockMvc.perform(post("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrow)))
                .andExpect(status().isBadRequest());

        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = borrowRepository.findAll().size();
        // set the field null
        borrow.setFee(null);

        // Create the Borrow, which fails.

        restBorrowMockMvc.perform(post("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrow)))
                .andExpect(status().isBadRequest());

        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBorrows() throws Exception {
        // Initialize the database
        borrowRepository.save(borrow);

        // Get all the borrows
        restBorrowMockMvc.perform(get("/api/borrows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(borrow.getId())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(DEFAULT_BORROW_DATE.toString())))
                .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
                .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.intValue())));
    }

    @Test
    public void getBorrow() throws Exception {
        // Initialize the database
        borrowRepository.save(borrow);

        // Get the borrow
        restBorrowMockMvc.perform(get("/api/borrows/{id}", borrow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(borrow.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.borrowDate").value(DEFAULT_BORROW_DATE.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.intValue()));
    }

    @Test
    public void getNonExistingBorrow() throws Exception {
        // Get the borrow
        restBorrowMockMvc.perform(get("/api/borrows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBorrow() throws Exception {
        // Initialize the database
        borrowService.save(borrow);

        int databaseSizeBeforeUpdate = borrowRepository.findAll().size();

        // Update the borrow
        Borrow updatedBorrow = borrowRepository.findOne(borrow.getId());
        updatedBorrow
                .userId(UPDATED_USER_ID)
                .isbn(UPDATED_ISBN)
                .borrowDate(UPDATED_BORROW_DATE)
                .returnDate(UPDATED_RETURN_DATE)
                .fee(UPDATED_FEE);

        restBorrowMockMvc.perform(put("/api/borrows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBorrow)))
                .andExpect(status().isOk());

        // Validate the Borrow in the database
        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeUpdate);
        Borrow testBorrow = borrows.get(borrows.size() - 1);
        assertThat(testBorrow.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBorrow.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBorrow.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testBorrow.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBorrow.getFee()).isEqualTo(UPDATED_FEE);
    }

    @Test
    public void deleteBorrow() throws Exception {
        // Initialize the database
        borrowService.save(borrow);

        int databaseSizeBeforeDelete = borrowRepository.findAll().size();

        // Get the borrow
        restBorrowMockMvc.perform(delete("/api/borrows/{id}", borrow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Borrow> borrows = borrowRepository.findAll();
        assertThat(borrows).hasSize(databaseSizeBeforeDelete - 1);
    }
}
