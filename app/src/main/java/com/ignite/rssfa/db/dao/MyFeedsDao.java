package com.ignite.rssfa.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ignite.rssfa.db.entity.Feed;

import java.util.List;

@Dao
public interface MyFeedsDao {

    @Query("SELECT * FROM MyFeeds")
    LiveData<List<Feed>> getAll();

    @Query("SELECT * FROM MyFeeds where title LIKE  :title")
    Feed findByTitle(String title);

    @Query("SELECT COUNT(*) from MyFeeds")
    int countFeeds();

    @Insert
    void insert(Feed feed);

    @Insert
    void insertAll(Feed... feeds);

    @Delete
    void delete(Feed feed);

    @Query("DELETE FROM MyFeeds WHERE title = :title")
    void deleteByTitle(String title);
}
