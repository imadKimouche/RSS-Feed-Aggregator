package com.ignite.rssfa.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ignite.rssfa.RsskeeArticle;
import com.ignite.rssfa.RsskeeArticleSaved;

import java.util.List;

@Dao
public interface SavedArticleDao {

    @Query("SELECT * FROM savedArticle")
    LiveData<List<RsskeeArticleSaved>> getAll();

    @Query("SELECT * FROM savedArticle where title LIKE  :title")
    RsskeeArticleSaved findByTitle(String title);

    @Query("SELECT COUNT(*) from savedArticle")
    int countUsers();

    @Insert
    void insert(RsskeeArticleSaved article);

    @Insert
    void insertAll(RsskeeArticleSaved... articleSaved);

    @Delete
    void delete(RsskeeArticleSaved articleSaved);

    @Query("DELETE FROM savedArticle WHERE title = :title")
    void deleteByTitle(String title);
}
