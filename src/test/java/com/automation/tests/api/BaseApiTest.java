package com.automation.tests.api;

import com.automation.core.RestAssuredConfiguration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe base para todos os testes de API.
 *
 * Arquitetura:
 * - Inicializa configuracoes do RestAssured uma unica vez
 * - Fornece logger para classes filhas
 * - Define ciclo de vida da instancia de teste
 *
 * Principios aplicados:
 * - DRY: Configuracoes centralizadas
 * - Template Method: Setup comum para todos os testes
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Configuracao inicial executada uma vez antes de todos os testes.
     * Inicializa RestAssured com configuracoes padrao.
     */
    @BeforeAll
    void setupApi() {
        logger.info("Inicializando configuracoes de teste de API");
        RestAssuredConfiguration.initialize();
        RestAssured.filters(new AllureRestAssured());
    }
}
