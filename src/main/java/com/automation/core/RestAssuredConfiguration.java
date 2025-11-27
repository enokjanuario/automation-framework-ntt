package com.automation.core;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Configuracao centralizada do RestAssured.
 *
 * Arquitetura:
 * - Singleton para configuracao global
 * - RequestSpecification e ResponseSpecification reutilizaveis
 * - Integracao com Allure para logging de requisicoes
 * - Configuracoes de timeout e retry centralizadas
 *
 * Principios SOLID aplicados:
 * - Single Responsibility: Apenas gerencia configuracao RestAssured
 * - Open/Closed: Extensivel atraves de metodos de factory
 * - Dependency Inversion: Depende de abstracoes (Configuration interface)
 */
public final class RestAssuredConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredConfiguration.class);
    private static final Configuration config = ConfigurationManager.getConfig();

    private RestAssuredConfiguration() {
        // Construtor privado para impedir instanciacao
    }

    /**
     * Inicializa configuracoes globais do RestAssured.
     * Deve ser chamado uma vez antes dos testes.
     */
    public static void initialize() {
        logger.info("Inicializando configuracoes do RestAssured");

        RestAssured.config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        logger.info("RestAssured configurado com sucesso");
    }

    /**
     * Cria RequestSpecification para API RESTCountries.
     *
     * @return RequestSpecification configurada
     */
    public static RequestSpecification getRestCountriesRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.restCountriesBaseUrl())
                .setBasePath("/v3.1")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .setConfig(getRestAssuredConfig())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria RequestSpecification para API JSONPlaceholder.
     *
     * @return RequestSpecification configurada
     */
    public static RequestSpecification getJsonPlaceholderRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.jsonPlaceholderBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .setConfig(getRestAssuredConfig())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria RequestSpecification customizada com base URI especifica.
     *
     * @param baseUri URI base da API
     * @return RequestSpecification configurada
     */
    public static RequestSpecification getCustomRequestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .setConfig(getRestAssuredConfig())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria ResponseSpecification com validacoes padrao.
     *
     * @return ResponseSpecification configurada
     */
    public static ResponseSpecification getDefaultResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria ResponseSpecification para respostas de sucesso (2xx).
     *
     * @return ResponseSpecification para sucesso
     */
    public static ResponseSpecification getSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(config.responseTimeout(), TimeUnit.MILLISECONDS))
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria ResponseSpecification para respostas de criacao (201).
     *
     * @return ResponseSpecification para criacao
     */
    public static ResponseSpecification getCreatedResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(config.responseTimeout(), TimeUnit.MILLISECONDS))
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Cria ResponseSpecification para respostas de erro (4xx).
     *
     * @param expectedStatusCode Codigo de status esperado
     * @return ResponseSpecification para erro
     */
    public static ResponseSpecification getErrorResponseSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Obtem configuracao do RestAssured com timeouts.
     *
     * @return RestAssuredConfig configurado
     */
    private static io.restassured.config.RestAssuredConfig getRestAssuredConfig() {
        return io.restassured.config.RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
                        .enablePrettyPrinting(true));
    }

    /**
     * Metodo auxiliar para comparacao de tempo de resposta.
     *
     * @param time Tempo maximo
     * @param unit Unidade de tempo
     * @return Matcher para tempo de resposta
     */
    private static org.hamcrest.Matcher<Long> lessThan(long time, TimeUnit unit) {
        return org.hamcrest.Matchers.lessThan(unit.toMillis(time));
    }
}
