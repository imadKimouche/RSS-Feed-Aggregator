package com.ignite.rssfa.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.ignite.rssfa.RsskeeArticle;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "MyFeeds")
public class Feed implements Parcelable {

    public Feed(int uid, String title, String link, String description, String language, String pubDate, String rssUrl, String image) {
        this.uid = uid;
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.pubDate = pubDate;
        this.rssUrl = rssUrl;
        this.image = image;
    }

    @Ignore
    public Feed(String title, String link, String description, String language, String pubDate, String rssUrl, String image) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.pubDate = pubDate;
        this.rssUrl = rssUrl;
        this.image = image;
    }

    @Ignore
    public Feed(int id, String title, String link, String description, String language, String pubDate, String rssUrl, String image, List<RsskeeArticle> articles) {
        this.uid = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.pubDate = pubDate;
        this.rssUrl = rssUrl;
        this.image = image;
        this.articles = articles;
    }

    public Feed(Parcel parcel) {
        uid = parcel.readInt();
        title = parcel.readString();
        articles = new ArrayList<>();
        rssUrl = parcel.readString();
        parcel.readList(articles, RsskeeArticle.class.getClassLoader());
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "link")
    private String link;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "language")
    private String language;

    @ColumnInfo(name = "pubDate")
    private String pubDate;

    @ColumnInfo(name = "rssUrl")
    private String rssUrl;

    @ColumnInfo(name = "image")
    private String image;

    @Ignore
    private List<RsskeeArticle> articles;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(title);
        dest.writeString(rssUrl);
        dest.writeList(articles);
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {

        @Override
        public Feed createFromParcel(Parcel parcel) {
            return new Feed(parcel);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[0];
        }
    };

    @Override
    public String toString() {
        return "{Feed:" + title +
                " desc=" + description +
                "}";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RsskeeArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<RsskeeArticle> articles) {
        this.articles = articles;
    }

    public void setArticlesFromParser(List<Article> articles) {
        for (int i = 0; i < articles.size(); i++) {
            this.articles.add(new RsskeeArticle(articles.get(i)));
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addArticle(RsskeeArticle article) {
        this.articles.add(article);
    }
}
