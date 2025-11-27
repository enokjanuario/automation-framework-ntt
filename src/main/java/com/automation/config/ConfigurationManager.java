package com.automation.config;

import org.aeonbits.owner.ConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gerenciador centralizado de configuracoes seguindo o padrao Singleton.
 *
 * Arquitetura:
 * - Singleton thread-safe atraves do ConfigCache do Owner
 * - Permite carregar configuracoes de multiplas fontes
 * - Suporta troca dinamica de ambiente via system property 'env'
 *
 * Uso:
 * Configuration config = ConfigurationManager.getConfig();
 * String baseUrl = config.webBaseUrl();
 */
public final class ConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

    private ConfigurationManager() {
        // Construtor privado para impedir instanciacao
    }

    /**
     * Obtem a instancia de configuracao.
     * Utiliza cache para garantir singleton thread-safe.
     *
     * @return Instancia de Configuration
     */
    public static Configuration getConfig() {
        return ConfigCache.getOrCreate(Configuration.class);
    }

    /**
     * Define o ambiente de execucao.
     * Deve ser chamado antes de qualquer acesso a configuracao.
     *
     * @param environment Nome do ambiente (dev, staging, prod)
     */
    public static void setEnvironment(String environment) {
        logger.info("Definindo ambiente de execucao: {}", environment);
        System.setProperty("env", environment);
        // Limpa o cache para recarregar com novo ambiente
        ConfigCache.clear();
    }

    /**
     * Obtem o ambiente atual de execucao.
     *
     * @return Nome do ambiente
     */
    public static String getCurrentEnvironment() {
        return getConfig().environment();
    }

    /**
     * Verifica se o ambiente atual e de producao.
     * Util para condicionar comportamentos especificos.
     *
     * @return true se ambiente for producao
     */
    public static boolean isProduction() {
        return "prod".equalsIgnoreCase(getCurrentEnvironment());
    }

    /**
     * Verifica se a execucao esta em modo headless.
     *
     * @return true se headless estiver habilitado
     */
    public static boolean isHeadless() {
        String headlessProperty = System.getProperty("browser.headless");
        if (headlessProperty != null) {
            return Boolean.parseBoolean(headlessProperty);
        }
        return getConfig().headless();
    }
}
