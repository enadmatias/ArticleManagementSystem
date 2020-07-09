package com.example.articlemanagementsystem.Model;

public class ArticleData {
    private String id;
    private String thumbnail;
    private String articleTitle;
    private String articleData;
    private String articleDate;
    private boolean isShrink = true;

    public ArticleData(String id,String thumbnail, String articleTitle, String articleData, String articleDate) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.articleTitle = articleTitle;
        this.articleData = articleData;
        this.articleDate = articleDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleData() {
        return articleData;
    }

    public void setArticleData(String articleData) {
        this.articleData = articleData;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public boolean isShrink() {
        return isShrink;
    }

    public void setShrink(boolean shrink) {
        isShrink = shrink;
    }
}
