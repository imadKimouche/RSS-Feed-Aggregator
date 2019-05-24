package com.ignite.rssfa.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ignite.rssfa.AlreadyRead;
import com.ignite.rssfa.RsskeeArticle;
import com.ignite.rssfa.RsskeeArticleSaved;
import com.ignite.rssfa.db.dao.AlreadyReadDao;
import com.ignite.rssfa.db.dao.FavoriteArticleDao;
import com.ignite.rssfa.db.dao.MyFeedsDao;
import com.ignite.rssfa.db.dao.SavedArticleDao;
import com.ignite.rssfa.db.entity.Feed;

@Database(entities = {RsskeeArticle.class, Feed.class, RsskeeArticleSaved.class, AlreadyRead.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract FavoriteArticleDao FavArticleDao();

    public abstract SavedArticleDao SavedArticleDao();

    public abstract AlreadyReadDao AlreadyReadDao();

    public abstract MyFeedsDao MyFeedsDao();

    public static final String DATABASE_NAME = "rsskee-db";

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance =
                            Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                                    .build();
                }
            }
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}