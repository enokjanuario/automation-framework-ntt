package com.automation.config;

/**
 * Enum que representa os ambientes disponiveis para execucao.
 *
 * Arquitetura:
 * - Define constantes para cada ambiente suportado
 * - Encapsula URLs e configuracoes especificas por ambiente
 * - Facilita extensao para novos ambientes
 */
public enum Environment {

    DEV("dev", "Ambiente de Desenvolvimento") {
        @Override
        public String getApiBaseUrl() {
            return "https://restcountries.com";
        }

        @Override
        public String getWebBaseUrl() {
            return "https://www.netshoes.com.br";
        }
    },

    STAGING("staging", "Ambiente de Homologacao") {
        @Override
        public String getApiBaseUrl() {
            return "https://restcountries.com";
        }

        @Override
        public String getWebBaseUrl() {
            return "https://www.netshoes.com.br";
        }
    },

    PROD("prod", "Ambiente de Producao") {
        @Override
        public String getApiBaseUrl() {
            return "https://restcountries.com";
        }

        @Override
        public String getWebBaseUrl() {
            return "https://www.netshoes.com.br";
        }
    };

    private final String name;
    private final String description;

    Environment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Obtem a URL base da API para este ambiente.
     *
     * @return URL base da API
     */
    public abstract String getApiBaseUrl();

    /**
     * Obtem a URL base da aplicacao Web para este ambiente.
     *
     * @return URL base Web
     */
    public abstract String getWebBaseUrl();

    /**
     * Converte string para enum Environment.
     *
     * @param envName Nome do ambiente
     * @return Environment correspondente ou DEV como padrao
     */
    public static Environment fromString(String envName) {
        for (Environment env : values()) {
            if (env.name.equalsIgnoreCase(envName)) {
                return env;
            }
        }
        return DEV;
    }
}
