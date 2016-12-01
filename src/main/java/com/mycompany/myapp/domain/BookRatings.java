package com.mycompany.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BookRatings.
 */

@Document(collection = "book_ratings")
public class BookRatings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    @Size(max = 13)
    @Field("isbn")
    private String isbn;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    @Field("rating")
    private Integer rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public BookRatings userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookRatings isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getRating() {
        return rating;
    }

    public BookRatings rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookRatings bookRatings = (BookRatings) o;
        if(bookRatings.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bookRatings.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookRatings{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", isbn='" + isbn + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
