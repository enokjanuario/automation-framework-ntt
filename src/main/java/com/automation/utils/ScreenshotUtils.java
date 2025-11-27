package com.automation.utils;

import com.automation.core.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitarios para captura de screenshots.
 *
 * Arquitetura:
 * - Integracao com Allure Report
 * - Salvamento em arquivo opcional
 * - Formatacao de nomes unicos
 */
public final class ScreenshotUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {
        // Construtor privado para impedir instanciacao
    }

    /**
     * Captura screenshot e anexa ao Allure.
     *
     * @param name Nome do screenshot
     */
    public static void captureAndAttach(String name) {
        logger.info("Capturando screenshot: {}", name);
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), "png");
        } catch (Exception e) {
            logger.error("Erro ao capturar screenshot: {}", e.getMessage());
        }
    }

    /**
     * Captura screenshot e salva em arquivo.
     *
     * @param name Nome base do arquivo
     * @return Path do arquivo salvo
     */
    public static Path captureAndSave(String name) {
        logger.info("Capturando e salvando screenshot: {}", name);
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            String timestamp = LocalDateTime.now().format(DATE_FORMAT);
            String filename = String.format("%s_%s.png", name, timestamp);
            Path filePath = dir.resolve(filename);

            Files.write(filePath, screenshot);
            logger.info("Screenshot salvo em: {}", filePath);

            return filePath;
        } catch (IOException e) {
            logger.error("Erro ao salvar screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Captura screenshot em caso de falha de teste.
     *
     * @param testName Nome do teste
     */
    public static void captureOnFailure(String testName) {
        String sanitizedName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
        captureAndAttach("Falha_" + sanitizedName);
        captureAndSave("failure_" + sanitizedName);
    }
}
