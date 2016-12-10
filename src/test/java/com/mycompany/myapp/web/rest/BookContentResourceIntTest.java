package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AiProjektApp;

import com.mycompany.myapp.domain.BookContent;
import com.mycompany.myapp.repository.BookContentRepository;
import com.mycompany.myapp.service.BookContentService;

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
 * Test class for the BookContentResource REST controller.
 *
 * @see BookContentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiProjektApp.class)
public class BookContentResourceIntTest {

    private static final String DEFAULT_BOOK_ID = "AAAAA";
    private static final String UPDATED_BOOK_ID = "BBBBB";

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    @Inject
    private BookContentRepository bookContentRepository;

    @Inject
    private BookContentService bookContentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookContentMockMvc;

    private BookContent bookContent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookContentResource bookContentResource = new BookContentResource();
        ReflectionTestUtils.setField(bookContentResource, "bookContentService", bookContentService);
        this.restBookContentMockMvc = MockMvcBuilders.standaloneSetup(bookContentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookContent createEntity() {
        BookContent bookContent = new BookContent()
                .bookId(DEFAULT_BOOK_ID)
                .content(DEFAULT_CONTENT);
        return bookContent;
    }

    @Before
    public void initTest() {
        bookContentRepository.deleteAll();
        bookContent = createEntity();
    }

    @Test
    public void createBookContent() throws Exception {
        int databaseSizeBeforeCreate = bookContentRepository.findAll().size();

        // Create the BookContent

        restBookContentMockMvc.perform(post("/api/book-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookContent)))
                .andExpect(status().isCreated());

        // Validate the BookContent in the database
        List<BookContent> bookContents = bookContentRepository.findAll();
        assertThat(bookContents).hasSize(databaseSizeBeforeCreate + 1);
        BookContent testBookContent = bookContents.get(bookContents.size() - 1);
        assertThat(testBookContent.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testBookContent.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    public void checkBookIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookContentRepository.findAll().size();
        // set the field null
        bookContent.setBookId(null);

        // Create the BookContent, which fails.

        restBookContentMockMvc.perform(post("/api/book-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookContent)))
                .andExpect(status().isBadRequest());

        List<BookContent> bookContents = bookContentRepository.findAll();
        assertThat(bookContents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookContentRepository.findAll().size();
        // set the field null
        bookContent.setContent(null);

        // Create the BookContent, which fails.

        restBookContentMockMvc.perform(post("/api/book-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookContent)))
                .andExpect(status().isBadRequest());

        List<BookContent> bookContents = bookContentRepository.findAll();
        assertThat(bookContents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBookContents() throws Exception {
        // Initialize the database
        bookContentRepository.save(bookContent);

        // Get all the bookContents
        restBookContentMockMvc.perform(get("/api/book-contents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bookContent.getId())))
                .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    public void getBookContent() throws Exception {
        // Initialize the database
        bookContentRepository.save(bookContent);

        // Get the bookContent
        restBookContentMockMvc.perform(get("/api/book-contents/{id}", bookContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookContent.getId()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    public void getNonExistingBookContent() throws Exception {
        // Get the bookContent
        restBookContentMockMvc.perform(get("/api/book-contents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBookContent() throws Exception {
        // Initialize the database
        bookContentService.save(bookContent);

        int databaseSizeBeforeUpdate = bookContentRepository.findAll().size();

        // Update the bookContent
        BookContent updatedBookContent = bookContentRepository.findOne(bookContent.getId());
        updatedBookContent
                .bookId(UPDATED_BOOK_ID)
                .content(UPDATED_CONTENT);

        restBookContentMockMvc.perform(put("/api/book-contents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBookContent)))
                .andExpect(status().isOk());

        // Validate the BookContent in the database
        List<BookContent> bookContents = bookContentRepository.findAll();
        assertThat(bookContents).hasSize(databaseSizeBeforeUpdate);
        BookContent testBookContent = bookContents.get(bookContents.size() - 1);
        assertThat(testBookContent.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testBookContent.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    public void deleteBookContent() throws Exception {
        // Initialize the database
        bookContentService.save(bookContent);

        int databaseSizeBeforeDelete = bookContentRepository.findAll().size();

        // Get the bookContent
        restBookContentMockMvc.perform(delete("/api/book-contents/{id}", bookContent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BookContent> bookContents = bookContentRepository.findAll();
        assertThat(bookContents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
