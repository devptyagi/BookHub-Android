package com.internshala.bookhub.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookEntity {
    @PrimaryKey @NonNull public String book_id;
    @ColumnInfo(name = "book_name") public String bookName;
    @ColumnInfo(name = "book_author") public String bookAuthor;
    @ColumnInfo(name = "book_price") public String bookPrice;
    @ColumnInfo(name = "book_rating") public String bookRating;
    @ColumnInfo(name = "book_desc") public String bookDesc;
    @ColumnInfo(name = "book_image") public String bookImage;

    public BookEntity(String book_id, String bookName, String bookAuthor, String bookPrice, String bookRating, String bookDesc, String bookImage) {
        this.book_id = book_id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookRating = bookRating;
        this.bookDesc = bookDesc;
        this.bookImage = bookImage;
    }

}
