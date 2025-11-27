package com.automation.services;

import com.automation.core.BaseRequest;
import com.automation.core.RestAssuredConfiguration;
import com.automation.models.api.Post;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service para API JSONPlaceholder.
 */
public class JsonPlaceholderService extends BaseRequest {

    private static final String POSTS_ENDPOINT = "/posts";
    private static final String POST_BY_ID_ENDPOINT = "/posts/{id}";
    private static final String COMMENTS_ENDPOINT = "/posts/{id}/comments";
    private static final String USERS_ENDPOINT = "/users";

    public JsonPlaceholderService() {
        super(RestAssuredConfiguration.getJsonPlaceholderRequestSpec());
    }

    /**
     * Lista todos os posts.
     *
     * @return Response com lista de posts
     */
    @Step("Listar todos os posts")
    public Response getAllPosts() {
        logger.info("Listando todos os posts");
        return doGet(POSTS_ENDPOINT);
    }

    /**
     * Obtem um post por ID.
     *
     * @param id ID do post
     * @return Response com o post
     */
    @Step("Obter post por ID: {id}")
    public Response getPostById(int id) {
        logger.info("Obtendo post por ID: {}", id);
        return doGet(POST_BY_ID_ENDPOINT, Map.of("id", id));
    }

    /**
     * Obtem um post como objeto.
     *
     * @param id ID do post
     * @return Post ou null se nao encontrado
     */
    @Step("Obter e deserializar post: {id}")
    public Post getPost(int id) {
        Response response = getPostById(id);
        if (response.getStatusCode() == 200) {
            return response.as(Post.class);
        }
        return null;
    }

    /**
     * Lista posts de um usuario especifico.
     *
     * @param userId ID do usuario
     * @return Response com posts do usuario
     */
    @Step("Listar posts do usuario: {userId}")
    public Response getPostsByUser(int userId) {
        logger.info("Listando posts do usuario: {}", userId);
        return doGetWithQuery(POSTS_ENDPOINT, Map.of("userId", userId));
    }

    /**
     * Obtem comentarios de um post.
     *
     * @param postId ID do post
     * @return Response com comentarios
     */
    @Step("Obter comentarios do post: {postId}")
    public Response getCommentsByPost(int postId) {
        logger.info("Obtendo comentarios do post: {}", postId);
        return doGet(COMMENTS_ENDPOINT, Map.of("id", postId));
    }

    /**
     * Cria um novo post.
     *
     * @param post Dados do post a criar
     * @return Response com o post criado
     */
    @Step("Criar novo post")
    public Response createPost(Post post) {
        logger.info("Criando novo post: {}", post);
        return doPost(POSTS_ENDPOINT, post);
    }

    /**
     * Atualiza um post existente (PUT - substituicao completa).
     *
     * @param id   ID do post
     * @param post Novos dados do post
     * @return Response com o post atualizado
     */
    @Step("Atualizar post: {id}")
    public Response updatePost(int id, Post post) {
        logger.info("Atualizando post {}: {}", id, post);
        return doPut("/posts/" + id, post);
    }

    /**
     * Atualiza parcialmente um post (PATCH).
     *
     * @param id   ID do post
     * @param post Campos a atualizar
     * @return Response com o post atualizado
     */
    @Step("Atualizar parcialmente post: {id}")
    public Response patchPost(int id, Post post) {
        logger.info("Atualizando parcialmente post {}: {}", id, post);
        return doPatch("/posts/" + id, post);
    }

    /**
     * Remove um post.
     *
     * @param id ID do post a remover
     * @return Response da operacao
     */
    @Step("Remover post: {id}")
    public Response deletePost(int id) {
        logger.info("Removendo post: {}", id);
        return doDelete("/posts/" + id);
    }

    /**
     * Verifica se um post existe.
     *
     * @param id ID do post
     * @return true se post existe
     */
    @Step("Verificar se post existe: {id}")
    public boolean postExists(int id) {
        return getPostById(id).getStatusCode() == 200;
    }

    /**
     * Obtem o total de posts.
     *
     * @return Quantidade de posts
     */
    @Step("Obter total de posts")
    public int getTotalPosts() {
        Response response = getAllPosts();
        if (response.getStatusCode() == 200) {
            return response.jsonPath().getList("$").size();
        }
        return 0;
    }

    /**
     * Converte response para lista de Posts.
     *
     * @param response Response da API
     * @return Lista de Posts
     */
    public List<Post> extractPosts(Response response) {
        return Arrays.asList(response.as(Post[].class));
    }

    /**
     * Extrai o ID de um post criado da response.
     *
     * @param response Response do POST
     * @return ID do post criado ou null
     */
    public Integer extractCreatedPostId(Response response) {
        try {
            return response.jsonPath().getInt("id");
        } catch (Exception e) {
            logger.warn("Erro ao extrair ID do post: {}", e.getMessage());
            return null;
        }
    }
}
