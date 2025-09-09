package org.skypro.skyshop.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {
    private final UUID id;
    private String articleTitle;
    private String articleText;

    public Article(UUID id, String articleTitle, String articleText) {
        this.id = id;
        this.articleTitle = articleTitle;
        this.articleText = articleText;
    }

    @Override
    public String toString() {
        return articleTitle + " " + articleText;
    }
    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return toString();
    }

    @Override
    @JsonIgnore
    public String getContentType() {
        return "ARTICLE";
    }

    @Override
    public String getName() {
        return articleTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(articleTitle, article.articleTitle) && Objects.equals(articleText, article.articleText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleTitle, articleText);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
