package com.automation.builders;

import com.automation.models.api.Post;
import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Builder para criacao de objetos Post com dados de teste.
 *
 * Arquitetura:
 * - Implementa Test Data Builder Pattern
 * - Utiliza Faker para geracao de dados aleatorios
 * - Permite customizacao fluente de cada campo
 * - Fornece metodos pre-configurados para cenarios comuns
 *
 * Uso:
 * Post post = new PostBuilder().build();                    // Post com dados aleatorios
 * Post post = new PostBuilder().withTitle("Teste").build(); // Post customizado
 * Post post = PostBuilder.aValidPost();                     // Post valido padrao
 */
public class PostBuilder {

    private static final Faker faker = new Faker(new Locale("pt-BR"));

    private Integer id;
    private String title;
    private String body;
    private Integer userId;

    /**
     * Construtor que inicializa com valores aleatorios.
     */
    public PostBuilder() {
        this.title = faker.lorem().sentence(5);
        this.body = faker.lorem().paragraph(3);
        this.userId = faker.number().numberBetween(1, 100);
    }

    /**
     * Cria um builder com dados totalmente vazios.
     *
     * @return Builder com campos nulos
     */
    public static PostBuilder anEmptyPost() {
        PostBuilder builder = new PostBuilder();
        builder.id = null;
        builder.title = null;
        builder.body = null;
        builder.userId = null;
        return builder;
    }

    /**
     * Cria um builder com Post valido padrao.
     *
     * @return Post valido
     */
    public static Post aValidPost() {
        return new PostBuilder().build();
    }

    /**
     * Cria um builder para cenario de criacao (POST).
     *
     * @return Post sem ID (para criacao)
     */
    public static Post aPostForCreation() {
        return new PostBuilder()
                .withId(null)
                .build();
    }

    /**
     * Cria um builder para cenario de atualizacao (PUT).
     *
     * @param id ID do post existente
     * @return Post com ID para atualizacao
     */
    public static Post aPostForUpdate(Integer id) {
        return new PostBuilder()
                .withId(id)
                .withTitle("Updated: " + faker.lorem().sentence(3))
                .withBody("Updated content: " + faker.lorem().paragraph(2))
                .build();
    }

    /**
     * Cria um Post com dados especificos da prova tecnica.
     *
     * @return Post conforme especificacao da prova
     */
    public static Post aPostFromSpecification() {
        return Post.builder()
                .title("foo")
                .body("bar")
                .userId(1)
                .build();
    }

    // ==================== Metodos Fluentes ====================

    public PostBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PostBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public PostBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public PostBuilder withRandomTitle() {
        this.title = faker.lorem().sentence(5);
        return this;
    }

    public PostBuilder withRandomBody() {
        this.body = faker.lorem().paragraph(3);
        return this;
    }

    public PostBuilder withEmptyTitle() {
        this.title = "";
        return this;
    }

    public PostBuilder withEmptyBody() {
        this.body = "";
        return this;
    }

    public PostBuilder withLongTitle(int words) {
        this.title = faker.lorem().sentence(words);
        return this;
    }

    public PostBuilder withLongBody(int paragraphs) {
        this.body = faker.lorem().paragraphs(paragraphs).toString();
        return this;
    }

    /**
     * Constroi o objeto Post final.
     *
     * @return Instancia de Post
     */
    public Post build() {
        return Post.builder()
                .id(id)
                .title(title)
                .body(body)
                .userId(userId)
                .build();
    }
}
