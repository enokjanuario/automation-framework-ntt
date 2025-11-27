package com.automation.core;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Gerenciador centralizado de WebDriver.
 *
 * Arquitetura:
 * - ThreadLocal para suporte a execucao paralela
 * - Factory Method para criacao de drivers
 * - Configuracao automatica via WebDriverManager
 *
 * Principios SOLID aplicados:
 * - Single Responsibility: Apenas gerenciamento de driver
 * - Open/Closed: Extensivel para novos navegadores
 * - Dependency Inversion: Configuracoes via interface
 */
public final class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final Configuration config = ConfigurationManager.getConfig();

    private DriverManager() {
        // Construtor privado para impedir instanciacao
    }

    /**
     * Obtem o WebDriver da thread atual.
     * Cria um novo driver se nao existir.
     *
     * @return WebDriver configurado
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver());
        }
        return driverThreadLocal.get();
    }

    /**
     * Cria um novo WebDriver baseado na configuracao.
     *
     * @return WebDriver configurado
     */
    private static WebDriver createDriver() {
        String browser = config.browser().toLowerCase();
        boolean headless = ConfigurationManager.isHeadless();

        logger.info("Criando WebDriver: {} (headless: {})", browser, headless);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge" -> createEdgeDriver(headless);
            default -> createChromeDriver(headless);
        };

        configureDriver(driver);
        return driver;
    }

    /**
     * Cria ChromeDriver com opcoes configuradas.
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return new ChromeDriver(options);
    }

    /**
     * Cria FirefoxDriver com opcoes configuradas.
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");

        if (headless) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return new FirefoxDriver(options);
    }

    /**
     * Cria EdgeDriver com opcoes configuradas.
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");

        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }

        return new EdgeDriver(options);
    }

    /**
     * Configura timeouts e comportamentos do driver.
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.implicitWait()));
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(config.pageLoadTimeout()));

        if (config.maximizeWindow()) {
            driver.manage().window().maximize();
        }

        logger.info("WebDriver configurado com sucesso");
    }

    /**
     * Encerra o WebDriver da thread atual.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Encerrando WebDriver");
            try {
                driver.quit();
            } catch (Exception e) {
                logger.warn("Erro ao encerrar WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Verifica se existe um driver ativo na thread atual.
     *
     * @return true se driver existe
     */
    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Reinicia o driver (quit + create).
     */
    public static void restartDriver() {
        quitDriver();
        driverThreadLocal.set(createDriver());
    }
}
