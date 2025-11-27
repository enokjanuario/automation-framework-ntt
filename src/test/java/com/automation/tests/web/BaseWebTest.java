package com.automation.tests.web;

import com.automation.config.ConfigurationManager;
import com.automation.core.DriverManager;
import com.automation.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * Classe base para todos os testes Web.
 *
 * Arquitetura:
 * - Gerencia ciclo de vida do WebDriver
 * - Captura screenshots em caso de falha
 * - Fornece logger para classes filhas
 *
 * Principios aplicados:
 * - Template Method: Setup/Teardown comuns
 * - DRY: Configuracoes centralizadas
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(BaseWebTest.ScreenshotTestWatcher.class)
public abstract class BaseWebTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Configuracao inicial antes de todos os testes da classe.
     */
    @BeforeAll
    void setupTestClass() {
        logger.info("========== Iniciando suite de testes Web ==========");
        logger.info("Ambiente: {}", ConfigurationManager.getCurrentEnvironment());
        logger.info("Browser: {}", ConfigurationManager.getConfig().browser());
        logger.info("Headless: {}", ConfigurationManager.isHeadless());
    }

    /**
     * Configuracao antes de cada teste.
     * Garante que o driver esta inicializado.
     */
    @BeforeEach
    void setupTest(TestInfo testInfo) {
        logger.info("---------- Iniciando teste: {} ----------", testInfo.getDisplayName());
        // Driver sera criado sob demanda pelo DriverManager
    }

    /**
     * Limpeza apos cada teste.
     * Captura screenshot se houve falha e fecha o driver.
     */
    @AfterEach
    void tearDownTest(TestInfo testInfo) {
        logger.info("---------- Finalizando teste: {} ----------", testInfo.getDisplayName());

        if (DriverManager.hasDriver()) {
            // Captura screenshot final para evidencia
            captureScreenshot("final_" + sanitizeTestName(testInfo.getDisplayName()));
            DriverManager.quitDriver();
        }
    }

    /**
     * Limpeza final apos todos os testes da classe.
     */
    @AfterAll
    void tearDownTestClass() {
        logger.info("========== Finalizando suite de testes Web ==========");
        // Garantia adicional de cleanup
        if (DriverManager.hasDriver()) {
            DriverManager.quitDriver();
        }
    }

    /**
     * Captura screenshot e anexa ao Allure.
     *
     * @param name Nome do screenshot
     */
    protected void captureScreenshot(String name) {
        try {
            if (DriverManager.hasDriver()) {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            logger.warn("Erro ao capturar screenshot: {}", e.getMessage());
        }
    }

    /**
     * Captura screenshot em caso de falha.
     *
     * @param testName Nome do teste
     */
    protected void captureScreenshotOnFailure(String testName) {
        ScreenshotUtils.captureOnFailure(testName);
    }

    /**
     * Sanitiza nome do teste para uso em arquivos.
     *
     * @param testName Nome original
     * @return Nome sanitizado
     */
    private String sanitizeTestName(String testName) {
        return testName.replaceAll("[^a-zA-Z0-9-_]", "_");
    }

    /**
     * Extension para capturar screenshots em falhas.
     */
    static class ScreenshotTestWatcher implements org.junit.jupiter.api.extension.TestWatcher {

        private static final Logger log = LoggerFactory.getLogger(ScreenshotTestWatcher.class);

        @Override
        public void testFailed(org.junit.jupiter.api.extension.ExtensionContext context, Throwable cause) {
            String testName = context.getDisplayName();
            log.error("Teste falhou: {} - Causa: {}", testName, cause.getMessage());

            if (DriverManager.hasDriver()) {
                ScreenshotUtils.captureOnFailure(testName);
            }
        }

        @Override
        public void testSuccessful(org.junit.jupiter.api.extension.ExtensionContext context) {
            log.info("Teste passou: {}", context.getDisplayName());
        }
    }
}
