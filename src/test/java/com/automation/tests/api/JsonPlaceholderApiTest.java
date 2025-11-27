package com.automation.tests.api;

import com.automation.builders.PostBuilder;
import com.automation.models.api.Post;
import com.automation.services.JsonPlaceholderService;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Testes de API para JSONPlaceholder.
 *
 * Arquitetura:
 * - Padrao AAA (Arrange-Act-Assert) explicito em cada teste
 * - Uso de Test Data Builder para criacao de dados
 * - Validacao de schema JSON
 * - Cobertura de operacoes CRUD
 *
 * Endpoints testados:
 * - GET /posts
 * - GET /posts/{id}
 * - POST /posts
 * - PUT /posts/{id}
 * - PATCH /posts/{id}
 * - DELETE /posts/{id}
 */
@Epic("API Testing")
@Feature("JSONPlaceholder API")
@DisplayName("Testes da API JSONPlaceholder")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonPlaceholderApiTest extends BaseApiTest {

    private JsonPlaceholderService jsonPlaceholderService;

    @BeforeEach
    void setUp() {
        jsonPlaceholderService = new JsonPlaceholderService();
    }

    // ==================== TESTES DE CRIACAO (POST) ====================

    @Test
    @Order(1)
    @Story("Criacao de Post")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC001 - Deve criar novo post com status 201 e retornar id gerado")
    @Description("Valida que a API cria um novo post e retorna status 201 com id gerado")
    void shouldCreateNewPostAndReturnGeneratedId() {
        // ===================== ARRANGE =====================
        // Preparacao: Cria objeto Post conforme especificacao da prova
        Post newPost = PostBuilder.aPostFromSpecification();
        logger.info("Preparando criacao de post: title='{}', body='{}', userId={}",
                newPost.getTitle(), newPost.getBody(), newPost.getUserId());

        // ===================== ACT =====================
        // Execucao: Realiza chamada POST para criar o recurso
        Response response = jsonPlaceholderService.createPost(newPost);
        logger.info("Requisicao POST executada. Status: {}", response.getStatusCode());

        // ===================== ASSERT =====================
        // Verificacao: Valida status 201 e presenca do id gerado
        response.then()
                .statusCode(201)
                .contentType(containsString("application/json"))
                .body("id", notNullValue())
                .body("id", isA(Integer.class));

        // Extrai o ID gerado para validacoes adicionais
        Integer generatedId = response.jsonPath().getInt("id");

        assertThat(generatedId)
                .as("O corpo da resposta deve conter um id gerado")
                .isNotNull()
                .isPositive();

        // Valida que os dados enviados foram retornados
        response.then()
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()))
                .body("userId", equalTo(newPost.getUserId()));

        logger.info("Teste TC001 finalizado com sucesso - Post criado com id: {}", generatedId);
    }

    @Test
    @Order(2)
    @Story("Criacao de Post")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC002 - Deve validar estrutura JSON da resposta de criacao")
    @Description("Valida que a resposta de criacao possui a estrutura JSON correta")
    void shouldValidateJsonStructureForPostCreation() {
        // ===================== ARRANGE =====================
        Post newPost = Post.builder()
                .title("Test Title for Schema Validation")
                .body("Test body content for schema validation")
                .userId(1)
                .build();

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.createPost(newPost);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));

        // Validacoes adicionais de tipos de dados
        response.then()
                .body("id", isA(Integer.class))
                .body("title", isA(String.class))
                .body("body", isA(String.class))
                .body("userId", isA(Integer.class));

        logger.info("Teste TC002 finalizado - Schema validado com sucesso");
    }

    @Test
    @Order(3)
    @Story("Criacao de Post")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC003 - Deve criar post com dados gerados pelo Builder")
    @Description("Valida criacao de post usando Test Data Builder com dados aleatorios")
    void shouldCreatePostWithBuilderGeneratedData() {
        // ===================== ARRANGE =====================
        // Usando Test Data Builder para gerar dados aleatorios
        Post newPost = PostBuilder.aValidPost();
        logger.info("Post gerado pelo builder: title='{}', userId={}",
                newPost.getTitle(), newPost.getUserId());

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.createPost(newPost);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(201);

        Integer generatedId = response.jsonPath().getInt("id");
        assertThat(generatedId)
                .as("Post deve ser criado com ID valido")
                .isNotNull()
                .isPositive();

        // Valida que os dados gerados foram persistidos
        String returnedTitle = response.jsonPath().getString("title");
        assertThat(returnedTitle)
                .as("Titulo retornado deve ser igual ao enviado")
                .isEqualTo(newPost.getTitle());

        logger.info("Teste TC003 finalizado - ID gerado: {}", generatedId);
    }

    // ==================== TESTES DE LEITURA (GET) ====================

    @Test
    @Order(4)
    @Story("Leitura de Posts")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC004 - Deve listar todos os posts com sucesso")
    @Description("Valida que a API retorna lista de posts com status 200")
    void shouldListAllPostsSuccessfully() {
        // ===================== ARRANGE =====================
        logger.info("Preparando listagem de todos os posts");

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.getAllPosts();

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("$", hasSize(greaterThan(0)));

        int totalPosts = response.jsonPath().getList("$").size();
        assertThat(totalPosts)
                .as("Deve retornar pelo menos 1 post")
                .isGreaterThan(0);

        logger.info("Teste TC004 finalizado - Total de posts: {}", totalPosts);
    }

    @Test
    @Order(5)
    @Story("Leitura de Posts")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC005 - Deve obter post especifico por ID")
    @Description("Valida que a API retorna um post especifico pelo ID")
    void shouldGetSpecificPostById() {
        // ===================== ARRANGE =====================
        int postId = 1;
        logger.info("Buscando post com ID: {}", postId);

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.getPostById(postId);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue());

        Post post = response.as(Post.class);
        assertThat(post.getId())
                .as("ID do post deve ser %d", postId)
                .isEqualTo(postId);

        logger.info("Teste TC005 finalizado - Post encontrado: '{}'", post.getTitle());
    }

    @Test
    @Order(6)
    @Story("Leitura de Posts")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC006 - Deve retornar 404 para post inexistente")
    @Description("Valida que a API retorna 404 quando busca post que nao existe")
    void shouldReturn404ForNonExistentPost() {
        // ===================== ARRANGE =====================
        int nonExistentId = 999999;
        logger.info("Buscando post inexistente com ID: {}", nonExistentId);

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.getPostById(nonExistentId);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(404);

        assertThat(response.getStatusCode())
                .as("Deve retornar 404 para ID inexistente")
                .isEqualTo(404);

        logger.info("Teste TC006 finalizado - 404 retornado conforme esperado");
    }

    // ==================== TESTES DE ATUALIZACAO (PUT/PATCH) ====================

    @Test
    @Order(7)
    @Story("Atualizacao de Post")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC007 - Deve atualizar post completamente (PUT)")
    @Description("Valida que a API atualiza todos os campos do post via PUT")
    void shouldUpdatePostCompletely() {
        // ===================== ARRANGE =====================
        int postId = 1;
        Post updatedPost = PostBuilder.aPostForUpdate(postId);
        logger.info("Atualizando post {}: title='{}'", postId, updatedPost.getTitle());

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.updatePost(postId, updatedPost);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("title", equalTo(updatedPost.getTitle()))
                .body("body", equalTo(updatedPost.getBody()));

        String returnedTitle = response.jsonPath().getString("title");
        assertThat(returnedTitle)
                .as("Titulo deve ser atualizado")
                .isEqualTo(updatedPost.getTitle());

        logger.info("Teste TC007 finalizado - Post atualizado com sucesso");
    }

    @Test
    @Order(8)
    @Story("Atualizacao de Post")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC008 - Deve atualizar parcialmente um post (PATCH)")
    @Description("Valida que a API atualiza apenas campos especificos via PATCH")
    void shouldPartiallyUpdatePost() {
        // ===================== ARRANGE =====================
        int postId = 1;
        Post partialUpdate = Post.builder()
                .title("Partially Updated Title via PATCH")
                .build();
        logger.info("Atualizando parcialmente post {}", postId);

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.patchPost(postId, partialUpdate);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("title", equalTo(partialUpdate.getTitle()));

        assertThat(response.jsonPath().getString("title"))
                .as("Titulo deve ser atualizado via PATCH")
                .isEqualTo(partialUpdate.getTitle());

        logger.info("Teste TC008 finalizado - PATCH aplicado com sucesso");
    }

    // ==================== TESTES DE REMOCAO (DELETE) ====================

    @Test
    @Order(9)
    @Story("Remocao de Post")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC009 - Deve remover post com sucesso")
    @Description("Valida que a API remove um post retornando status 200")
    void shouldDeletePostSuccessfully() {
        // ===================== ARRANGE =====================
        int postId = 1;
        logger.info("Removendo post com ID: {}", postId);

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.deletePost(postId);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200);

        assertThat(response.getStatusCode())
                .as("DELETE deve retornar status 200")
                .isEqualTo(200);

        logger.info("Teste TC009 finalizado - Post removido com sucesso");
    }

    // ==================== TESTES DE PERFORMANCE ====================

    @Test
    @Order(10)
    @Story("Performance")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC010 - Deve responder em tempo aceitavel para POST")
    @Description("Valida que a operacao POST responde em tempo aceitavel")
    void shouldRespondWithinAcceptableTimeForPost() {
        // ===================== ARRANGE =====================
        Post newPost = PostBuilder.aPostForCreation();
        long maxResponseTimeMs = 3000;

        // ===================== ACT =====================
        Response response = jsonPlaceholderService.createPost(newPost);

        // ===================== ASSERT =====================
        long responseTime = response.getTime();

        assertThat(responseTime)
                .as("Tempo de resposta deve ser menor que %dms", maxResponseTimeMs)
                .isLessThan(maxResponseTimeMs);

        logger.info("Teste TC010 finalizado - Tempo de resposta: {}ms", responseTime);
    }

    // ==================== METODO AUXILIAR PARA SCHEMA ====================

    private static JsonSchemaValidator matchesJsonSchemaInClasspath(String schemaPath) {
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath);
    }
}
