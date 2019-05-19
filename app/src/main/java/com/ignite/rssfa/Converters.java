package com.ignite.rssfa;

import android.arch.persistence.room.TypeConverter;

import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Article> fromString(String value) {
        List<Article> articles = new ArrayList<>();
        String[] stringArticles = value.split("\n");

        for (int i = 0; i < stringArticles.length; i++) {
            Article article = new Article();
            String[] stringArticle = stringArticles[i].split("\t");
            article.setTitle(stringArticle[0]);
            article.setDescription(stringArticle[1]);
            article.setContent(stringArticle[2]);
            article.setImage(stringArticle[3]);
            article.setPubDate(stringArticle[4]);
            article.setAuthor(stringArticle[5]);
            articles.add(article);
        }
        return articles;
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Article> list) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            Article article = list.get(i);
            result.append(article.getTitle() + "\t" + article.getDescription() + "\t" + article.getContent() + "\t" + article.getImage() + "\t" + article.getPubDate() + "\t" + article.getAuthor() + "\n");
        }
        return String.valueOf(result);
    }
}