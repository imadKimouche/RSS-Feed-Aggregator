package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ignite.rssfa.RsskeeArticle;
import com.ignite.rssfa.db.dao.FavoriteArticleDao;
import com.ignite.rssfa.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavArticleRepository {
    private FavoriteArticleDao mFavArticleDao;
    private LiveData<List<RsskeeArticle>> mFavArticles;

    FavArticleRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mFavArticleDao = db.FavArticleDao();
        mFavArticles = mFavArticleDao.getAll();
    }

    public LiveData<List<RsskeeArticle>> getAllFavArticles() {
        return mFavArticles;
    }

    public Boolean insert(RsskeeArticle article) throws ExecutionException, InterruptedException {
        AsyncTask<RsskeeArticle, Void, Boolean> task = new insertAsyncTask(mFavArticleDao).execute(article);
        return task.get();
    }

    private static class insertAsyncTask extends AsyncTask<RsskeeArticle, Void, Boolean> {

        private FavoriteArticleDao mAsyncTaskDao;

        insertAsyncTask(FavoriteArticleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RsskeeArticle... params) {
            if (mAsyncTaskDao.findByTitle(params[0].getTitle()) != null) {
                mAsyncTaskDao.deleteByTitle(params[0].getTitle());
                return false;
            } else
                mAsyncTaskDao.insert(params[0]);
            return true;
        }

/*        @Override // Maybe use next
        protected void onPostExecute(Boolean result) {
            Log.i("DEBUG", result ? "Rss inserted" : "Rss Not inserted");
        }*/
    }

    public Boolean articleFavExists(RsskeeArticle article) throws ExecutionException, InterruptedException {
        AsyncTask<RsskeeArticle, Void, Boolean> task = new existsAsyncTask(mFavArticleDao).execute(article);
        return task.get();
    }

    private static class existsAsyncTask extends AsyncTask<RsskeeArticle, Void, Boolean> {

        private FavoriteArticleDao mAsyncTaskDao;

        existsAsyncTask(FavoriteArticleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final RsskeeArticle... params) {
            return mAsyncTaskDao.findByTitle(params[0].getTitle()) != null;
        }
    }
}
