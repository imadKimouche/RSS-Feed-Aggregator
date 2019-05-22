package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ignite.rssfa.RsskeeArticle;
import com.ignite.rssfa.db.FavArticleRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavArticleViewModel extends AndroidViewModel {

    private FavArticleRepository mRepository;

    private LiveData<List<RsskeeArticle>> mFavArticles;

    public FavArticleViewModel(Application application) {
        super(application);
        mRepository = new FavArticleRepository(application);
        mFavArticles = mRepository.getAllFavArticles();
    }

    public LiveData<List<RsskeeArticle>> getAllFavRss() {
        return mFavArticles;
    }

    public Boolean insert(RsskeeArticle article) {
        Boolean result = false;
        try {
            result = mRepository.insert(article);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean exists(RsskeeArticle article) {
        Boolean result = false;
        try {
            result = mRepository.articleFavExists(article);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}