package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AiProjektApp;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.repository.BookRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiProjektApp.class)
public class BookResourceIntTest {

    private static final String DEFAULT_ISBN = "AAAAAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    private static final Integer DEFAULT_YEAR_OF_PUBLICATION = 1;
    private static final Integer UPDATED_YEAR_OF_PUBLICATION = 2;

    private static final String DEFAULT_IMAGE_URL_S = "AAAAA";
    private static final String UPDATED_IMAGE_URL_S = "BBBBB";

    private static final String DEFAULT_IMAGE_URL_M = "AAAAA";
    private static final String UPDATED_IMAGE_URL_M = "BBBBB";

    private static final String DEFAULT_IMAGE_URL_L = "AAAAA";
    private static final String UPDATED_IMAGE_URL_L = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Inject
    private BookRepository bookRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookMockMvc;

    private Book book;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource();
        ReflectionTestUtils.setField(bookResource, "bookRepository", bookRepository);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity() {
        Book book = new Book()
                .isbn(DEFAULT_ISBN)
                .title(DEFAULT_TITLE)
                .author(DEFAULT_AUTHOR)
                .yearOfPublication(DEFAULT_YEAR_OF_PUBLICATION)
                .imageUrlS(DEFAULT_IMAGE_URL_S)
                .imageUrlM(DEFAULT_IMAGE_URL_M)
                .imageUrlL(DEFAULT_IMAGE_URL_L)
                .price(DEFAULT_PRICE);
        return book;
    }

    @Before
    public void initTest() {
        bookRepository.deleteAll();
        book = createEntity();
    }

    @Test
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBook.getYearOfPublication()).isEqualTo(DEFAULT_YEAR_OF_PUBLICATION);
        assertThat(testBook.getImageUrlS()).isEqualTo(DEFAULT_IMAGE_URL_S);
        assertThat(testBook.getImageUrlM()).isEqualTo(DEFAULT_IMAGE_URL_M);
        assertThat(testBook.getImageUrlL()).isEqualTo(DEFAULT_IMAGE_URL_L);
        assertThat(testBook.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    public void checkIsbnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setIsbn(null);

        // Create the Book, which fails.

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setPrice(null);

        // Create the Book, which fails.

        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get all the books
        restBookMockMvc.perform(get("/api/books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].yearOfPublication").value(hasItem(DEFAULT_YEAR_OF_PUBLICATION)))
                .andExpect(jsonPath("$.[*].imageUrlS").value(hasItem(DEFAULT_IMAGE_URL_S.toString())))
                .andExpect(jsonPath("$.[*].imageUrlM").value(hasItem(DEFAULT_IMAGE_URL_M.toString())))
                .andExpect(jsonPath("$.[*].imageUrlL").value(hasItem(DEFAULT_IMAGE_URL_L.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.yearOfPublication").value(DEFAULT_YEAR_OF_PUBLICATION))
            .andExpect(jsonPath("$.imageUrlS").value(DEFAULT_IMAGE_URL_S.toString()))
            .andExpect(jsonPath("$.imageUrlM").value(DEFAULT_IMAGE_URL_M.toString()))
            .andExpect(jsonPath("$.imageUrlL").value(DEFAULT_IMAGE_URL_L.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findOne(book.getId());
        updatedBook
                .isbn(UPDATED_ISBN)
                .title(UPDATED_TITLE)
                .author(UPDATED_AUTHOR)
                .yearOfPublication(UPDATED_YEAR_OF_PUBLICATION)
                .imageUrlS(UPDATED_IMAGE_URL_S)
                .imageUrlM(UPDATED_IMAGE_URL_M)
                .imageUrlL(UPDATED_IMAGE_URL_L)
                .price(UPDATED_PRICE);

        restBookMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBook)))
                .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeUpdate);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBook.getYearOfPublication()).isEqualTo(UPDATED_YEAR_OF_PUBLICATION);
        assertThat(testBook.getImageUrlS()).isEqualTo(UPDATED_IMAGE_URL_S);
        assertThat(testBook.getImageUrlM()).isEqualTo(UPDATED_IMAGE_URL_M);
        assertThat(testBook.getImageUrlL()).isEqualTo(UPDATED_IMAGE_URL_L);
        assertThat(testBook.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.save(book);
        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeDelete - 1);
    }
}
