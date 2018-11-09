package com.semanticanalyzer.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Setter
public class ArticleEntry {
    private String id;
    private String url;
    private String title;
    private String description;
    private String publicationDate;

    public ArticleEntry(String id, String url, String title, String description, Timestamp publicationDate) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate.toInstant().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate.toInstant().toString();
    }
}
