package com.vedas.weightloss.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Rise on 14/06/2018.
 */

public class NewsFeedObject {

    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String author;

    public String getAuthor() { return this.author; }

    public void setAuthor(String author) { this.author = author; }

    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    private String description;

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    private String url;

    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }

    private String filetype;

    public String getFiletype() { return this.filetype; }

    public void setFiletype(String filetype) { this.filetype = filetype; }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String thumbnail;


    private Date publishedAt;

    public Date getPublishedAt() { return this.publishedAt; }

    public void setPublishedAt(Date publishedAt) { this.publishedAt = publishedAt; }

}
