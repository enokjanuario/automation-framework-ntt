package com.automation.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modelo que representa um Post da API JSONPlaceholder.
 *
 * Arquitetura:
 * - Builder pattern para criacao fluente de objetos
 * - Jackson annotations para serializacao/deserializacao JSON
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    private Integer userId;

    public Post() {
    }

    public Post(Integer id, String title, String body, Integer userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Integer getUserId() {
        return userId;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Valida se o Post possui todos os campos obrigatorios preenchidos.
     *
     * @return true se o Post e valido
     */
    public boolean isValid() {
        return title != null && !title.isEmpty()
                && body != null && !body.isEmpty()
                && userId != null && userId > 0;
    }

    /**
     * Cria uma copia do Post para uso em atualizacoes.
     *
     * @return Nova instancia de Post com mesmos valores
     */
    public Post copy() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .userId(this.userId)
                .build();
    }

    public static PostBuilder builder() {
        return new PostBuilder();
    }

    /**
     * Builder class for Post.
     */
    public static class PostBuilder {
        private Integer id;
        private String title;
        private String body;
        private Integer userId;

        PostBuilder() {
        }

        public PostBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public PostBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder body(String body) {
            this.body = body;
            return this;
        }

        public PostBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Post build() {
            return new Post(id, title, body, userId);
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return java.util.Objects.equals(id, post.id) &&
                java.util.Objects.equals(title, post.title) &&
                java.util.Objects.equals(body, post.body) &&
                java.util.Objects.equals(userId, post.userId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, body, userId);
    }
}
