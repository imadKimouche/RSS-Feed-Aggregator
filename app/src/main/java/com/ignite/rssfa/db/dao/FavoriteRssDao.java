package com.ignite.rssfa.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ignite.rssfa.db.entity.RSS;

import java.util.List;

@Dao
public interface FavoriteRssDao {

    @Query("SELECT * FROM favoriteRss")
    LiveData<List<RSS>> getAll();

    @Query("SELECT * FROM favoriteRss where title LIKE  :title")
    RSS findByTitle(String title);

    @Query("SELECT COUNT(*) from favoriteRss")
    int countUsers();

    @Insert
    void insert(RSS rss);

    @Insert
    void insertAll(RSS... rssFavorites);

    @Delete
    void delete(RSS rssFavorite);

    @Query("DELETE FROM favoriteRss WHERE title = :title")
    void deleteByTitle(String title);
}
