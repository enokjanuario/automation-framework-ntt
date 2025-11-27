package com.automation.tests.api;

import com.automation.services.RestCountriesService;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Testes de API para RESTCountries.
 *
 * Arquitetura:
 * - Padrao AAA (Arrange-Act-Assert) explicito em cada teste
 * - Validacao de schema JSON
 * - Validacao de estrutura e conteudo
 * - Cobertura de cenarios positivos e negativos
 *
 * Endpoints testados:
 * - GET /v3.1/name/{name}
 */
@Epic("API Testing")
@Feature("RESTCountries API")
@DisplayName("Testes da API RESTCountries")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestCountriesApiTest extends BaseApiTest {

    private RestCountriesService restCountriesService;

    @BeforeEach
    void setUp() {
        restCountriesService = new RestCountriesService();
    }

    // ==================== TESTES DE BUSCA POR NOME ====================

    @Test
    @Order(1)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC001 - Deve retornar status 200 ao buscar pais por nome valido (Canada)")
    @Description("Valida que a API retorna status 200 quando busca por um pais existente")
    void shouldReturn200WhenSearchingValidCountryName() {
        // ===================== ARRANGE =====================
        // Preparacao: Define o nome do pais a ser buscado
        String countryName = "canada";
        logger.info("Preparando busca pelo pais: {}", countryName);

        // ===================== ACT =====================
        // Execucao: Realiza a chamada GET para buscar o pais
        Response response = restCountriesService.getCountryByName(countryName);
        logger.info("Requisicao executada. Status: {}", response.getStatusCode());

        // ===================== ASSERT =====================
        // Verificacao: Valida o status code e estrutura da resposta
        response.then()
                .statusCode(200)
                .contentType(containsString("application/json"));

        // Validacao adicional com AssertJ
        assertThat(response.getStatusCode())
                .as("Status code deve ser 200 para pais existente")
                .isEqualTo(200);

        logger.info("Teste TC001 finalizado com sucesso");
    }

    @Test
    @Order(2)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC002 - Deve validar estrutura JSON da resposta para busca por nome")
    @Description("Valida que a resposta possui a estrutura JSON correta conforme schema")
    void shouldValidateJsonStructureForCountrySearch() {
        // ===================== ARRANGE =====================
        // Preparacao: Define o pais para validacao de schema
        String countryName = "canada";
        logger.info("Preparando validacao de schema para: {}", countryName);

        // ===================== ACT =====================
        // Execucao: Realiza a chamada GET
        Response response = restCountriesService.getCountryByName(countryName);

        // ===================== ASSERT =====================
        // Verificacao: Valida estrutura JSON com schema
        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/country-schema.json"));

        // Validacoes adicionais de campos obrigatorios
        response.then()
                .body("[0].name", notNullValue())
                .body("[0].name.common", notNullValue())
                .body("[0].name.official", notNullValue())
                .body("[0].cca2", notNullValue())
                .body("[0].cca3", notNullValue())
                .body("[0].region", notNullValue());

        logger.info("Teste TC002 finalizado - Schema validado com sucesso");
    }

    @Test
    @Order(3)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC003 - Deve validar campo name.common retorna 'Canada'")
    @Description("Valida que o campo name.common da resposta contem exatamente 'Canada'")
    void shouldValidateCommonNameFieldForCanada() {
        // ===================== ARRANGE =====================
        // Preparacao: Define valor esperado do campo
        String countryName = "canada";
        String expectedCommonName = "Canada";
        logger.info("Preparando validacao do campo name.common para: {}", countryName);

        // ===================== ACT =====================
        // Execucao: Realiza a chamada GET
        Response response = restCountriesService.getCountryByName(countryName);

        // ===================== ASSERT =====================
        // Verificacao: Valida o campo name.common
        String actualCommonName = response.jsonPath().getString("[0].name.common");

        assertThat(actualCommonName)
                .as("O campo name.common deve ser '%s'", expectedCommonName)
                .isEqualTo(expectedCommonName);

        // Validacao adicional com Hamcrest
        response.then()
                .body("[0].name.common", equalTo(expectedCommonName));

        logger.info("Teste TC003 finalizado - name.common = {}", actualCommonName);
    }

    @Test
    @Order(4)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC004 - Deve retornar campos adicionais corretamente para Canada")
    @Description("Valida campos adicionais como capital, region, subregion para Canada")
    void shouldValidateAdditionalFieldsForCanada() {
        // ===================== ARRANGE =====================
        String countryName = "canada";
        String expectedCapital = "Ottawa";
        String expectedRegion = "Americas";

        // ===================== ACT =====================
        Response response = restCountriesService.getCountryByName(countryName);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("[0].capital[0]", equalTo(expectedCapital))
                .body("[0].region", equalTo(expectedRegion))
                .body("[0].subregion", equalTo("North America"))
                .body("[0].languages.eng", equalTo("English"))
                .body("[0].languages.fra", equalTo("French"));

        // Validacao com AssertJ para dados numericos
        Long population = response.jsonPath().getLong("[0].population");
        assertThat(population)
                .as("Populacao deve ser maior que 30 milhoes")
                .isGreaterThan(30000000L);

        logger.info("Teste TC004 finalizado - Campos adicionais validados");
    }

    @ParameterizedTest
    @Order(5)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC005 - Deve aceitar busca case-insensitive")
    @ValueSource(strings = {"canada", "CANADA", "Canada", "CaNaDa"})
    @Description("Valida que a API aceita variações de case no nome do país")
    void shouldAcceptCaseInsensitiveSearch(String countryName) {
        // ===================== ARRANGE =====================
        logger.info("Testando case-insensitive com: {}", countryName);

        // ===================== ACT =====================
        Response response = restCountriesService.getCountryByName(countryName);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("[0].name.common", equalTo("Canada"));

        assertThat(response.getStatusCode())
                .as("API deve aceitar '%s' e retornar 200", countryName)
                .isEqualTo(200);
    }

    @Test
    @Order(6)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC006 - Deve retornar 404 para pais inexistente")
    @Description("Valida que a API retorna 404 quando busca por um pais que nao existe")
    void shouldReturn404ForNonExistentCountry() {
        // ===================== ARRANGE =====================
        String invalidCountryName = "paisquenaoexiste123xyz";
        logger.info("Testando pais inexistente: {}", invalidCountryName);

        // ===================== ACT =====================
        Response response = restCountriesService.getCountryByName(invalidCountryName);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(404);

        assertThat(response.getStatusCode())
                .as("Deve retornar 404 para pais inexistente")
                .isEqualTo(404);

        logger.info("Teste TC006 finalizado - 404 retornado conforme esperado");
    }

    @Test
    @Order(7)
    @Story("Busca por Nome")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC007 - Deve retornar multiplos resultados para busca parcial")
    @Description("Valida que a API retorna lista quando busca retorna multiplos paises")
    void shouldReturnMultipleResultsForPartialSearch() {
        // ===================== ARRANGE =====================
        String partialName = "united";
        logger.info("Testando busca parcial com: {}", partialName);

        // ===================== ACT =====================
        Response response = restCountriesService.getCountryByName(partialName);

        // ===================== ASSERT =====================
        response.then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(1)));

        int resultCount = response.jsonPath().getList("$").size();
        assertThat(resultCount)
                .as("Busca por '%s' deve retornar multiplos paises", partialName)
                .isGreaterThan(1);

        logger.info("Teste TC007 finalizado - {} paises encontrados", resultCount);
    }

    @Test
    @Order(8)
    @Story("Performance")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC008 - Deve responder em tempo aceitavel (< 5 segundos)")
    @Description("Valida que a API responde em tempo aceitavel")
    void shouldRespondWithinAcceptableTime() {
        // ===================== ARRANGE =====================
        String countryName = "canada";
        long maxResponseTimeMs = 5000;

        // ===================== ACT =====================
        Response response = restCountriesService.getCountryByName(countryName);

        // ===================== ASSERT =====================
        long responseTime = response.getTime();

        assertThat(responseTime)
                .as("Tempo de resposta deve ser menor que %dms", maxResponseTimeMs)
                .isLessThan(maxResponseTimeMs);

        logger.info("Teste TC008 finalizado - Tempo de resposta: {}ms", responseTime);
    }

    // ==================== METODO AUXILIAR PARA SCHEMA ====================

    private static JsonSchemaValidator matchesJsonSchemaInClasspath(String schemaPath) {
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath);
    }
}
