package com.ignite.rssfa.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ignite.rssfa.db.entity.RSS;

@Database(entities = {RSS.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;
    public abstract com.ignite.rssfa.db.dao.FavoriteRssDao FavRssDao();
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

        private final com.ignite.rssfa.db.dao.FavoriteRssDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.FavRssDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            RSS rss = new RSS("rss", "rss", "rss");
            mDao.insert(rss);
            return null;
        }
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}