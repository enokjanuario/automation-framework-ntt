package com.automation.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Classe base para requisicoes HTTP.
 *
 * Arquitetura:
 * - Encapsula operacoes HTTP comuns (GET, POST, PUT, PATCH, DELETE)
 * - Centraliza logging e tratamento de erros
 * - Fornece metodos reutilizaveis para todas as services
 *
 * Principios SOLID aplicados:
 * - Single Responsibility: Apenas operacoes HTTP
 * - Open/Closed: Extensivel para novos metodos HTTP
 */
public abstract class BaseRequest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected RequestSpecification requestSpec;

    protected BaseRequest(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    /**
     * Executa requisicao GET.
     *
     * @param path Caminho do endpoint
     * @return Response da requisicao
     */
    protected Response doGet(String path) {
        logger.info("Executando GET: {}", path);
        return given()
                .spec(requestSpec)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao GET com path parameters.
     *
     * @param path       Caminho do endpoint com placeholders
     * @param pathParams Parametros do path
     * @return Response da requisicao
     */
    protected Response doGet(String path, Map<String, ?> pathParams) {
        logger.info("Executando GET: {} com params: {}", path, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao GET com query parameters.
     *
     * @param path        Caminho do endpoint
     * @param queryParams Query parameters
     * @return Response da requisicao
     */
    protected Response doGetWithQuery(String path, Map<String, ?> queryParams) {
        logger.info("Executando GET: {} com query: {}", path, queryParams);
        return given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao POST.
     *
     * @param path Caminho do endpoint
     * @param body Corpo da requisicao
     * @return Response da requisicao
     */
    protected Response doPost(String path, Object body) {
        logger.info("Executando POST: {} com body: {}", path, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao POST sem body.
     *
     * @param path Caminho do endpoint
     * @return Response da requisicao
     */
    protected Response doPost(String path) {
        logger.info("Executando POST: {}", path);
        return given()
                .spec(requestSpec)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao PUT.
     *
     * @param path Caminho do endpoint
     * @param body Corpo da requisicao
     * @return Response da requisicao
     */
    protected Response doPut(String path, Object body) {
        logger.info("Executando PUT: {} com body: {}", path, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao PATCH.
     *
     * @param path Caminho do endpoint
     * @param body Corpo da requisicao
     * @return Response da requisicao
     */
    protected Response doPatch(String path, Object body) {
        logger.info("Executando PATCH: {} com body: {}", path, body);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao DELETE.
     *
     * @param path Caminho do endpoint
     * @return Response da requisicao
     */
    protected Response doDelete(String path) {
        logger.info("Executando DELETE: {}", path);
        return given()
                .spec(requestSpec)
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    /**
     * Executa requisicao DELETE com path parameters.
     *
     * @param path       Caminho do endpoint com placeholders
     * @param pathParams Parametros do path
     * @return Response da requisicao
     */
    protected Response doDelete(String path, Map<String, ?> pathParams) {
        logger.info("Executando DELETE: {} com params: {}", path, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }
}
