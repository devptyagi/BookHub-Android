package com.internshala.bookhub.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    public void insertBook(BookEntity bookEntity);

    @Delete
    public void deletBook(BookEntity bookEntity);

    @Query("SELECT * FROM books")
    public List<BookEntity> getAllBooks();

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    public BookEntity getBookById(String bookId);
}
