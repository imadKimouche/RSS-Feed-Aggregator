package com.ignite.rssfa.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ignite.rssfa.RsskeeArticle;

import java.util.List;

@Dao
public interface FavoriteArticleDao {

    @Query("SELECT * FROM favoriteArticle")
    LiveData<List<RsskeeArticle>> getAll();

    @Query("SELECT * FROM favoriteArticle where title LIKE  :title")
    RsskeeArticle findByTitle(String title);

    @Query("SELECT COUNT(*) from favoriteArticle")
    int countUsers();

    @Insert
    void insert(RsskeeArticle article);

    @Insert
    void insertAll(RsskeeArticle... articleFavorites);

    @Delete
    void delete(RsskeeArticle articleFavorite);

    @Query("DELETE FROM favoriteArticle WHERE title = :title")
    void deleteByTitle(String title);
}
