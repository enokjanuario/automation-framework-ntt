package com.automation.services;

import com.automation.core.BaseRequest;
import com.automation.core.RestAssuredConfiguration;
import com.automation.models.api.Country;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service para API RESTCountries.
 */
public class RestCountriesService extends BaseRequest {

    private static final String NAME_ENDPOINT = "/name/{name}";
    private static final String CURRENCY_ENDPOINT = "/v3.1/currency/{currency}";
    private static final String LANGUAGE_ENDPOINT = "/v3.1/lang/{language}";
    private static final String ALPHA_ENDPOINT = "/alpha/{code}";
    private static final String ALL_ENDPOINT = "/all";

    public RestCountriesService() {
        super(RestAssuredConfiguration.getRestCountriesRequestSpec());
    }

    /**
     * Busca paises por nome.
     *
     * @param name Nome do pais (parcial ou completo)
     * @return Response da requisicao
     */
    @Step("Buscar pais por nome: {name}")
    public Response getCountryByName(String name) {
        logger.info("Buscando pais por nome: {}", name);
        return doGet(NAME_ENDPOINT, Map.of("name", name));
    }

    /**
     * Busca pais por nome exato (fullText=true).
     *
     * @param name Nome exato do pais
     * @return Response da requisicao
     */
    @Step("Buscar pais por nome exato: {name}")
    public Response getCountryByExactName(String name) {
        logger.info("Buscando pais por nome exato: {}", name);
        return doGetWithQuery("/name/" + name, Map.of("fullText", "true"));
    }

    /**
     * Busca paises por nome e converte para lista de objetos.
     *
     * @param name Nome do pais
     * @return Lista de Country ou lista vazia se nao encontrado
     */
    @Step("Buscar e deserializar paises por nome: {name}")
    public List<Country> getCountriesByName(String name) {
        Response response = getCountryByName(name);
        if (response.getStatusCode() == 200) {
            return Arrays.asList(response.as(Country[].class));
        }
        return List.of();
    }

    /**
     * Busca paises por codigo de moeda.
     *
     * @param currency Codigo da moeda (ex: USD, EUR)
     * @return Response da requisicao
     */
    @Step("Buscar paises por moeda: {currency}")
    public Response getCountriesByCurrency(String currency) {
        logger.info("Buscando paises por moeda: {}", currency);
        return doGet(CURRENCY_ENDPOINT, Map.of("currency", currency));
    }

    /**
     * Busca paises por idioma.
     *
     * @param language Idioma (ex: english, spanish)
     * @return Response da requisicao
     */
    @Step("Buscar paises por idioma: {language}")
    public Response getCountriesByLanguage(String language) {
        logger.info("Buscando paises por idioma: {}", language);
        return doGet(LANGUAGE_ENDPOINT, Map.of("language", language));
    }

    /**
     * Busca pais por codigo alpha (cca2 ou cca3).
     *
     * @param code Codigo do pais (ex: BR, BRA)
     * @return Response da requisicao
     */
    @Step("Buscar pais por codigo: {code}")
    public Response getCountryByCode(String code) {
        logger.info("Buscando pais por codigo: {}", code);
        return doGet(ALPHA_ENDPOINT, Map.of("code", code));
    }

    /**
     * Obtem lista de todos os paises.
     *
     * @return Response da requisicao
     */
    @Step("Buscar todos os paises")
    public Response getAllCountries() {
        logger.info("Buscando todos os paises");
        return doGet(ALL_ENDPOINT);
    }

    /**
     * Verifica se um pais existe por nome.
     *
     * @param name Nome do pais
     * @return true se pais existe
     */
    @Step("Verificar se pais existe: {name}")
    public boolean countryExists(String name) {
        return getCountryByName(name).getStatusCode() == 200;
    }

    /**
     * Obtem o nome comum de um pais da resposta.
     *
     * @param response Response da API
     * @return Nome comum do primeiro pais ou null
     */
    public String extractCommonName(Response response) {
        try {
            return response.jsonPath().getString("[0].name.common");
        } catch (Exception e) {
            logger.warn("Erro ao extrair nome comum: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Obtem o nome oficial de um pais da resposta.
     *
     * @param response Response da API
     * @return Nome oficial do primeiro pais ou null
     */
    public String extractOfficialName(Response response) {
        try {
            return response.jsonPath().getString("[0].name.official");
        } catch (Exception e) {
            logger.warn("Erro ao extrair nome oficial: {}", e.getMessage());
            return null;
        }
    }
}
