package com.internshala.bookhub.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BookEntity.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

}
