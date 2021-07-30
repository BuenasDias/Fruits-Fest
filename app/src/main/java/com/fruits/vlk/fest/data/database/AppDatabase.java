package com.fruits.vlk.fest.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.fruits.vlk.fest.data.dao.UserDao;
import com.fruits.vlk.fest.data.entities.User;


@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao mUserDao();
}
