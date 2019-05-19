package com.ignite.rssfa;

import java.util.List;

import com.ignite.rssfa.db.entity.Feed;

public class Topic {

    private String title;
    private String image;
    private List<Feed> mFeeds;

    public Topic(String title, String image, List<Feed> feeds) {
        this.title = title;
        this.image = image;
        this.mFeeds = feeds;
    }

    public List<Feed> getmFeeds() {
        return mFeeds;
    }

    public void setmFeeds(List<Feed> mFeeds) {
        this.mFeeds = mFeeds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
