package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.BookRatings;
import com.mycompany.myapp.domain.Borrow;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.BookRatingsService;
import com.mycompany.myapp.service.BookService;
import com.mycompany.myapp.service.BorrowService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.web.rest.vm.Rating;
import com.mycompany.myapp.web.rest.vm.TopBorrows;
import com.mycompany.myapp.web.rest.vm.TopRatings;
import com.mycompany.myapp.web.rest.vm.TopUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Borrow.
 */
@RestController
@RequestMapping("/api")
public class Statistics {

    private final Logger log = LoggerFactory.getLogger(Statistics.class);

    @Inject
    private BorrowService borrowService;
    @Inject
    private BookRatingsService bookRatingsService;
    @Inject
    private BookService bookService;
    @Inject
    private UserService userService;

    @GetMapping("/statistics/users")
    @Timed
    public ResponseEntity<List<TopUser>> getTopUsers()
        throws URISyntaxException {
        log.debug("REST request to get top users");
        List<Borrow> borrows = borrowService.findAll();
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(Borrow b : borrows)
        {
            if(b.isPaid()) {
                String userId = b.getUserId();
                if (map.containsKey(userId)) {
                    int newValue = map.get(userId) + 1;
                    map.replace(userId, newValue);
                } else {
                    map.put(userId, 1);
                }
            }
        }
        List<TopUser> result = new ArrayList<TopUser>();
        map = sortByValue(map);
        int i = 0;
        Iterator it = map.entrySet().iterator();
        Map.Entry<String,Integer> entry;
        while(i<10 && it.hasNext())
        {
            entry = (Map.Entry)it.next();
            User user = userService.getUserWithAuthorities(entry.getKey());
            if(user!=null)
            {
                TopUser newTopUser = new TopUser();
                newTopUser.borrows = entry.getValue();
                newTopUser.eMail = user.getEmail();
                newTopUser.firstName = user.getFirstName();
                newTopUser.lastName = user.getLastName();
                result.add(newTopUser);
            }
            it.remove();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/statistics/borrows")
    @Timed
    public ResponseEntity<List<TopBorrows>> getTopBorrows()
        throws URISyntaxException {
        log.debug("REST request to get top users");
        List<Borrow> borrows = borrowService.findAll();
        log.debug("Tworzenie listy - borrows size " + borrows.size());
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(Borrow b : borrows)
        {
            if(b.isPaid()) {
                String isbn = b.getIsbn();
                if (map.containsKey(isbn)) {
                    int newValue = map.get(isbn) + 1;
                    map.replace(isbn, newValue);
                } else {
                    map.put(isbn, 1);
                }
            }
        }
        List<TopBorrows> result = new ArrayList<TopBorrows>();
        map = sortByValue(map);
        int i = 0;
        Iterator it = map.entrySet().iterator();
        Map.Entry<String, Integer> entry;
        while(i<10 && it.hasNext())
        {
            entry = (Map.Entry)it.next();
            log.debug("Tworzenie listy: " + entry.getKey() + ":" + entry.getValue());
            Book book = bookService.findOneByIsbn(entry.getKey());
            if(book != null)
                log.debug("Tworzenie listy - book: " + book.getIsbn());
            else
                log.debug("Tworzenie listy - book: nullowa ");


            if(book!=null)
            {
                TopBorrows newTopBorrow = new TopBorrows();
                newTopBorrow.bookAuthor = book.getAuthor();
                newTopBorrow.bookName = book.getTitle();
                newTopBorrow.isbn = book.getIsbn();
                newTopBorrow.borrows = entry.getValue();
                result.add(newTopBorrow);
            }
            it.remove();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/statistics/bookRatings")
    @Timed
    public ResponseEntity<List<TopRatings>> getTopRatings()
        throws URISyntaxException {

        List<BookRatings> ratings =  bookRatingsService.findAll();
        Map<String, Rating> map = new HashMap<String, Rating>();
        for(BookRatings b : ratings)
        {
                String isbn = b.getIsbn();
                if (map.containsKey(isbn)) {
                    Rating rating = map.get(isbn);
                    rating.Add(b.getRating());
                    map.replace(isbn, rating);
                } else {
                    Rating newRating = new Rating();
                    newRating.Add(b.getRating());
                    map.put(isbn, newRating);
                }
        }
        List<TopRatings> result = new ArrayList<TopRatings>();
        map = sortRatingsByValue(map);
        int i = 0;
        Iterator it = map.entrySet().iterator();
        Map.Entry<String, Rating> entry;
        while(i<10 && it.hasNext())
        {
            entry = (Map.Entry)it.next();
            Book book = bookService.findOneByIsbn(entry.getKey());
            if(book!=null)
            {
                TopRatings newTopRatings = new TopRatings();
                newTopRatings.bookAuthor = book.getAuthor();
                newTopRatings.bookName = book.getTitle();
                newTopRatings.isbn = book.getIsbn();
                newTopRatings.averageRating = entry.getValue().GetAverage();
                result.add(newTopRatings);
            }
            i++;
            it.remove();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public <K, V> Map<String, Integer> sortByValue(Map<String, Integer> map) {
        return map.entrySet()
            .stream()
            .sorted(comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    public <String, Rating extends Comparable<Rating>> Map<String, Rating> sortRatingsByValue(Map<String, Rating> map) {
        return map.entrySet()
            .stream()
            .sorted(comparingRatingsByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
        return (Comparator<Map.Entry<K, V>> & Serializable)
            (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }

    public static <String, Rating extends Comparable<Rating>> Comparator<Map.Entry<String,Rating>> comparingRatingsByValue() {
        return (Comparator<Map.Entry<String, Rating>> & Serializable)
            (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }
}
