package com.mycompany.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Borrow.
 */

@Document(collection = "borrow")
public class Borrow implements Serializable {

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
    @Field("borrow_date")
    private LocalDate borrowDate;

    @Field("return_date")
    private LocalDate returnDate;

    @NotNull
    @DecimalMin(value = "0")
    @Field("fee")
    private BigDecimal fee;

    @Field("paid")
    private Boolean paid = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public Borrow userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public Borrow isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public Borrow borrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Borrow returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public Borrow fee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Boolean isPaid() {
        return paid;
    }

    public Borrow paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Borrow borrow = (Borrow) o;
        if(borrow.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, borrow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Borrow{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", isbn='" + isbn + "'" +
            ", borrowDate='" + borrowDate + "'" +
            ", returnDate='" + returnDate + "'" +
            ", fee='" + fee + "'" +
            ", paid='" + paid + "'" +
            '}';
    }
}
