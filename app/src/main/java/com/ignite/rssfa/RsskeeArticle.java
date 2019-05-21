package com.ignite.rssfa;

import android.os.Parcel;
import android.os.Parcelable;

import com.prof.rssparser.Article;

public class RsskeeArticle implements Parcelable {
    private String title;
    private String description;
    private String content;
    private String image;
    private String pubDate;
    private String author;
    private String url;

    public RsskeeArticle(String title, String description, String content, String image, String pubDate, String author, String url) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.image = image;
        this.pubDate = pubDate;
        this.author = author;
        this.url = url;
    }

    public RsskeeArticle(Article article) {
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.content = article.getContent() == null ? "" : article.getContent();
        this.image = article.getImage();
        this.pubDate = article.getPubDate();
        this.author = article.getAuthor();
        this.url = article.getLink();
    }

    protected RsskeeArticle(Parcel in) {
        title = in.readString();
        description = in.readString();
        content = in.readString();
        image = in.readString();
        pubDate = in.readString();
        author = in.readString();
        url = in.readString();
    }

    public static final Creator<RsskeeArticle> CREATOR = new Creator<RsskeeArticle>() {
        @Override
        public RsskeeArticle createFromParcel(Parcel in) {
            return new RsskeeArticle(in);
        }

        @Override
        public RsskeeArticle[] newArray(int size) {
            return new RsskeeArticle[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(pubDate);
        dest.writeString(author);
        dest.writeString(url);
    }

    @Override
    public String toString() {

        return "authror: " + author + " title: " + title + " desc: " + description + " content: " + content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
