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
    private String content;
    private Timestamp publicationDate;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate;
    }
}
