package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AiProjektApp;

import com.mycompany.myapp.domain.BookRatings;
import com.mycompany.myapp.repository.BookRatingsRepository;
import com.mycompany.myapp.service.BookRatingsService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookRatingsResource REST controller.
 *
 * @see BookRatingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiProjektApp.class)
public class BookRatingsResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAA";
    private static final String UPDATED_USER_ID = "BBBBB";

    private static final String DEFAULT_ISBN = "AAAAAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;

    @Inject
    private BookRatingsRepository bookRatingsRepository;

    @Inject
    private BookRatingsService bookRatingsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookRatingsMockMvc;

    private BookRatings bookRatings;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookRatingsResource bookRatingsResource = new BookRatingsResource();
        ReflectionTestUtils.setField(bookRatingsResource, "bookRatingsService", bookRatingsService);
        this.restBookRatingsMockMvc = MockMvcBuilders.standaloneSetup(bookRatingsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookRatings createEntity() {
        BookRatings bookRatings = new BookRatings()
                .userId(DEFAULT_USER_ID)
                .isbn(DEFAULT_ISBN)
                .rating(DEFAULT_RATING);
        return bookRatings;
    }

    @Before
    public void initTest() {
        bookRatingsRepository.deleteAll();
        bookRatings = createEntity();
    }

    @Test
    public void createBookRatings() throws Exception {
        int databaseSizeBeforeCreate = bookRatingsRepository.findAll().size();

        // Create the BookRatings

        restBookRatingsMockMvc.perform(post("/api/book-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookRatings)))
                .andExpect(status().isCreated());

        // Validate the BookRatings in the database
        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeCreate + 1);
        BookRatings testBookRatings = bookRatings.get(bookRatings.size() - 1);
        assertThat(testBookRatings.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBookRatings.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBookRatings.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRatingsRepository.findAll().size();
        // set the field null
        bookRatings.setUserId(null);

        // Create the BookRatings, which fails.

        restBookRatingsMockMvc.perform(post("/api/book-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookRatings)))
                .andExpect(status().isBadRequest());

        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkIsbnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRatingsRepository.findAll().size();
        // set the field null
        bookRatings.setIsbn(null);

        // Create the BookRatings, which fails.

        restBookRatingsMockMvc.perform(post("/api/book-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookRatings)))
                .andExpect(status().isBadRequest());

        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRatingsRepository.findAll().size();
        // set the field null
        bookRatings.setRating(null);

        // Create the BookRatings, which fails.

        restBookRatingsMockMvc.perform(post("/api/book-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookRatings)))
                .andExpect(status().isBadRequest());

        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBookRatings() throws Exception {
        // Initialize the database
        bookRatingsRepository.save(bookRatings);

        // Get all the bookRatings
        restBookRatingsMockMvc.perform(get("/api/book-ratings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bookRatings.getId())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    public void getBookRatings() throws Exception {
        // Initialize the database
        bookRatingsRepository.save(bookRatings);

        // Get the bookRatings
        restBookRatingsMockMvc.perform(get("/api/book-ratings/{id}", bookRatings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookRatings.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    public void getNonExistingBookRatings() throws Exception {
        // Get the bookRatings
        restBookRatingsMockMvc.perform(get("/api/book-ratings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBookRatings() throws Exception {
        // Initialize the database
        bookRatingsService.save(bookRatings);

        int databaseSizeBeforeUpdate = bookRatingsRepository.findAll().size();

        // Update the bookRatings
        BookRatings updatedBookRatings = bookRatingsRepository.findOne(bookRatings.getId());
        updatedBookRatings
                .userId(UPDATED_USER_ID)
                .isbn(UPDATED_ISBN)
                .rating(UPDATED_RATING);

        restBookRatingsMockMvc.perform(put("/api/book-ratings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBookRatings)))
                .andExpect(status().isOk());

        // Validate the BookRatings in the database
        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeUpdate);
        BookRatings testBookRatings = bookRatings.get(bookRatings.size() - 1);
        assertThat(testBookRatings.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBookRatings.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBookRatings.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    public void deleteBookRatings() throws Exception {
        // Initialize the database
        bookRatingsService.save(bookRatings);

        int databaseSizeBeforeDelete = bookRatingsRepository.findAll().size();

        // Get the bookRatings
        restBookRatingsMockMvc.perform(delete("/api/book-ratings/{id}", bookRatings.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        assertThat(bookRatings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
