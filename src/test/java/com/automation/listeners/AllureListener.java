package com.automation.listeners;

import com.automation.core.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * Listener customizado para integracao com Allure Report.
 *
 * Arquitetura:
 * - Captura screenshots automaticamente em falhas
 * - Adiciona informacoes de ambiente ao relatorio
 * - Log detalhado de status dos testes
 *
 * Uso:
 * @ExtendWith(AllureListener.class)
 * class MinhaClasseDeTeste { ... }
 */
public class AllureListener implements TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(AllureListener.class);

    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = getTestName(context);
        logger.info("PASSOU: {}", testName);
        captureScreenshot(testName + "_sucesso");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = getTestName(context);
        logger.error("FALHOU: {} - Causa: {}", testName, cause.getMessage());

        // Captura screenshot em caso de falha
        captureScreenshot(testName + "_falha");

        // Adiciona detalhes do erro ao Allure
        Allure.addAttachment("Mensagem de Erro", cause.getMessage());
        if (cause.getStackTrace().length > 0) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : cause.getStackTrace()) {
                stackTrace.append(element.toString()).append("\n");
            }
            Allure.addAttachment("Stack Trace", stackTrace.toString());
        }
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = getTestName(context);
        logger.warn("ABORTADO: {} - Causa: {}", testName, cause.getMessage());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = getTestName(context);
        logger.info("DESABILITADO: {} - Motivo: {}", testName, reason.orElse("Nao especificado"));
    }

    /**
     * Captura screenshot e anexa ao relatorio Allure.
     *
     * @param name Nome do screenshot
     */
    private void captureScreenshot(String name) {
        try {
            if (DriverManager.hasDriver()) {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
                logger.debug("Screenshot capturado: {}", name);
            }
        } catch (Exception e) {
            logger.warn("Erro ao capturar screenshot: {}", e.getMessage());
        }
    }

    /**
     * Obtem nome formatado do teste.
     *
     * @param context Contexto do teste
     * @return Nome do teste
     */
    private String getTestName(ExtensionContext context) {
        return context.getDisplayName();
    }
}
