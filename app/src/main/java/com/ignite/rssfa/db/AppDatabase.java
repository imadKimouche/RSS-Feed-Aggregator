package com.ignite.rssfa.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ignite.rssfa.Converters;
import com.ignite.rssfa.RsskeeArticle;
import com.ignite.rssfa.RsskeeArticleSaved;
import com.ignite.rssfa.db.dao.FavoriteArticleDao;
import com.ignite.rssfa.db.dao.MyFeedsDao;
import com.ignite.rssfa.db.dao.SavedArticleDao;
import com.ignite.rssfa.db.entity.Feed;

@Database(entities = {RsskeeArticle.class, Feed.class, RsskeeArticleSaved.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;
    public abstract FavoriteArticleDao FavArticleDao();
    public abstract SavedArticleDao SavedArticleDao();
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

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(sInstance).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FavoriteArticleDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.FavArticleDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            RsskeeArticle article = new RsskeeArticle("", "", "", "", "", "", "");
            mDao.insert(article);
            return null;
        }
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}