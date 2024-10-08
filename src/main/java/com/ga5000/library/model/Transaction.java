package com.ga5000.library.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Date transactionDate;

    @Column
    private Date returnDate;

    @Column(nullable = false)
    private boolean finished;

    public Transaction(){}

    public Transaction(Long id, Book book, Member member, TransactionType transactionType, Date transactionDate, Date returnDate, boolean finished) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.returnDate = returnDate;
        this.finished = finished;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
