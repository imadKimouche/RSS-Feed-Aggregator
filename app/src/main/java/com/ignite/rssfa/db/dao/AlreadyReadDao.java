package com.ignite.rssfa.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ignite.rssfa.AlreadyRead;

@Dao
public interface AlreadyReadDao {

    @Query("SELECT * FROM alreadyRead where url LIKE  :url")
    AlreadyRead findByUrl(String url);

    @Query("SELECT COUNT(*) from alreadyRead")
    int countAlreadyRead();

    @Insert
    void insert(AlreadyRead article);

    @Insert
    void insertAll(AlreadyRead... articles);
}
