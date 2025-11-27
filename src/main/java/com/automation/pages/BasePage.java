package com.automation.pages;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import com.automation.core.DriverManager;
import com.automation.utils.ElementUtils;
import com.automation.utils.ScreenshotUtils;
import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Classe base para todas as Page Objects.
 */
public abstract class BasePage {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;
    protected final Configuration config;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = ConfigurationManager.getConfig();
        PageFactory.initElements(driver, this);
    }

    /**
     * Navega para URL especifica.
     *
     * @param url URL de destino
     */
    @Step("Navegar para: {url}")
    protected void navigateTo(String url) {
        logger.info("Navegando para: {}", url);
        driver.get(url);
        WaitUtils.waitForPageLoad();
    }

    /**
     * Navega para URL base da aplicacao.
     */
    @Step("Navegar para URL base")
    protected void navigateToBaseUrl() {
        navigateTo(config.webBaseUrl());
    }

    /**
     * Obtem URL atual.
     *
     * @return URL atual
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Obtem titulo da pagina.
     *
     * @return Titulo da pagina
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Recarrega a pagina atual.
     */
    @Step("Recarregar pagina")
    protected void refresh() {
        logger.info("Recarregando pagina");
        driver.navigate().refresh();
        WaitUtils.waitForPageLoad();
    }

    /**
     * Volta para pagina anterior.
     */
    @Step("Voltar pagina")
    protected void goBack() {
        logger.info("Voltando para pagina anterior");
        driver.navigate().back();
        WaitUtils.waitForPageLoad();
    }

    /**
     * Clica em elemento.
     *
     * @param locator Localizador do elemento
     */
    protected void click(By locator) {
        ElementUtils.click(locator);
    }

    /**
     * Preenche campo de texto.
     *
     * @param locator Localizador do elemento
     * @param text    Texto a preencher
     */
    protected void type(By locator, String text) {
        ElementUtils.type(locator, text);
    }

    /**
     * Obtem texto do elemento.
     *
     * @param locator Localizador do elemento
     * @return Texto do elemento
     */
    protected String getText(By locator) {
        return ElementUtils.getText(locator);
    }

    /**
     * Obtem valor do campo.
     *
     * @param locator Localizador do elemento
     * @return Valor do campo
     */
    protected String getValue(By locator) {
        return ElementUtils.getValue(locator);
    }

    /**
     * Verifica se elemento esta visivel.
     *
     * @param locator Localizador do elemento
     * @return true se visivel
     */
    protected boolean isVisible(By locator) {
        return ElementUtils.isVisible(locator);
    }

    /**
     * Verifica se elemento esta habilitado.
     *
     * @param locator Localizador do elemento
     * @return true se habilitado
     */
    protected boolean isEnabled(By locator) {
        return ElementUtils.isEnabled(locator);
    }

    /**
     * Faz scroll ate elemento.
     *
     * @param locator Localizador do elemento
     */
    protected void scrollTo(By locator) {
        ElementUtils.scrollToElement(locator);
    }

    /**
     * Seleciona opcao por texto visivel.
     *
     * @param locator Localizador do select
     * @param text    Texto da opcao
     */
    protected void selectByText(By locator, String text) {
        ElementUtils.selectByText(locator, text);
    }

    /**
     * Aguarda elemento ficar visivel.
     *
     * @param locator Localizador do elemento
     * @return WebElement visivel
     */
    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(locator);
    }

    /**
     * Aguarda elemento ficar clicavel.
     *
     * @param locator Localizador do elemento
     * @return WebElement clicavel
     */
    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(locator);
    }

    /**
     * Aguarda elemento desaparecer.
     *
     * @param locator Localizador do elemento
     * @return true quando invisivel
     */
    protected boolean waitForInvisible(By locator) {
        return WaitUtils.waitForInvisible(locator);
    }

    /**
     * Aguarda texto estar presente no elemento.
     *
     * @param locator Localizador do elemento
     * @param text    Texto esperado
     * @return true quando texto presente
     */
    protected boolean waitForText(By locator, String text) {
        return WaitUtils.waitForTextPresent(locator, text);
    }

    /**
     * Aguarda URL conter texto.
     *
     * @param urlPart Parte da URL
     * @return true quando URL contem
     */
    protected boolean waitForUrlContains(String urlPart) {
        return WaitUtils.waitForUrlContains(urlPart);
    }

    /**
     * Captura screenshot da pagina atual.
     *
     * @param name Nome do screenshot
     */
    protected void captureScreenshot(String name) {
        ScreenshotUtils.captureAndAttach(name);
    }

    /**
     * Verifica se a pagina esta carregada corretamente.
     *
     * @return true se pagina carregada
     */
    public abstract boolean isPageLoaded();

    /**
     * Obtem o nome da pagina para logging.
     *
     * @return Nome da pagina
     */
    protected abstract String getPageName();
}
