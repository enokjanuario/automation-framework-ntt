package com.automation.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

/**
 * Interface de configuracao centralizada utilizando Owner API.
 * Carrega propriedades de diferentes fontes de forma hierarquica.
 *
 * Arquitetura:
 * - Utiliza Owner API para gerenciamento de configuracoes
 * - Suporta multiplos ambientes (dev, staging, prod)
 * - Valores podem ser sobrescritos via system properties
 */
@LoadPolicy(LoadType.MERGE)
@Sources({
    "system:properties",
    "system:env",
    "classpath:config/${env}.properties",
    "classpath:config/default.properties"
})
public interface Configuration extends Config {

    // ==================== API Configuration ====================

    @Key("api.restcountries.base.url")
    @DefaultValue("https://restcountries.com")
    String restCountriesBaseUrl();

    @Key("api.jsonplaceholder.base.url")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String jsonPlaceholderBaseUrl();

    @Key("api.timeout.connection")
    @DefaultValue("10000")
    int connectionTimeout();

    @Key("api.timeout.response")
    @DefaultValue("30000")
    int responseTimeout();

    @Key("api.retry.count")
    @DefaultValue("3")
    int retryCount();

    @Key("api.logging.enabled")
    @DefaultValue("true")
    boolean apiLoggingEnabled();

    // ==================== Web Configuration ====================

    @Key("web.base.url")
    @DefaultValue("https://www.netshoes.com.br")
    String webBaseUrl();

    @Key("web.browser")
    @DefaultValue("chrome")
    String browser();

    @Key("headless")
    @DefaultValue("false")
    boolean headless();

    @Key("web.timeout.implicit")
    @DefaultValue("10")
    int implicitWait();

    @Key("web.timeout.explicit")
    @DefaultValue("15")
    int explicitWait();

    @Key("web.timeout.page.load")
    @DefaultValue("30")
    int pageLoadTimeout();

    @Key("web.window.maximize")
    @DefaultValue("true")
    boolean maximizeWindow();

    @Key("web.screenshots.on.failure")
    @DefaultValue("true")
    boolean screenshotsOnFailure();

    // ==================== Environment ====================

    @Key("env")
    @DefaultValue("dev")
    String environment();

    @Key("parallel.enabled")
    @DefaultValue("true")
    boolean parallelEnabled();

    @Key("parallel.threads")
    @DefaultValue("4")
    int parallelThreads();
}
