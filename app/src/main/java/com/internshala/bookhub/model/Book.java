package com.internshala.bookhub.model;

public class Book {

    public String bookId;
    public String bookName;
    public String bookAuthor;
    public String bookRating;
    public String bookPrice;
    public String bookImage;

    public Book(String bookId, String bookName, String bookAuthor, String bookRating, String bookPrice, String bookImage) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookRating = bookRating;
        this.bookPrice = bookPrice;
        this.bookImage = bookImage;
    }


}
