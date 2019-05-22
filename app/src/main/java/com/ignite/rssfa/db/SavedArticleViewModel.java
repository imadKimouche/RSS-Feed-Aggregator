package com.ignite.rssfa.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ignite.rssfa.RsskeeArticleSaved;
import com.ignite.rssfa.db.SavedArticleRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SavedArticleViewModel extends AndroidViewModel {

    private SavedArticleRepository mRepository;

    private LiveData<List<RsskeeArticleSaved>> mArticleList;

    public SavedArticleViewModel(Application application) {
        super(application);
        mRepository = new SavedArticleRepository(application);
        mArticleList = mRepository.getAllSavedArticles();
    }

    public LiveData<List<RsskeeArticleSaved>> getAllSavedArticles() {
        return mArticleList;
    }

    public Boolean insert(RsskeeArticleSaved article) {
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

    public Boolean exists(RsskeeArticleSaved article) {
        Boolean result = false;
        try {
            result = mRepository.savedArticleExists(article);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}