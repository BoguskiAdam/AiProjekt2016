package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Borrow;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.BookService;
import com.mycompany.myapp.service.BorrowService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
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
    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
        return (Comparator<Map.Entry<K, V>> & Serializable)
            (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }
}
