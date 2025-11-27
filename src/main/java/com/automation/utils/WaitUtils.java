package com.automation.utils;

import com.automation.config.Configuration;
import com.automation.config.ConfigurationManager;
import com.automation.core.DriverManager;
import com.automation.exceptions.WaitTimeoutException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Utilitarios avancados de espera explicita.
 */
public final class WaitUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private static final Configuration config = ConfigurationManager.getConfig();

    private WaitUtils() {
    }

    /**
     * Obtem WebDriverWait padrao.
     *
     * @return WebDriverWait configurado
     */
    public static WebDriverWait getDefaultWait() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(config.explicitWait())
        );
    }

    /**
     * Obtem WebDriverWait com timeout customizado.
     *
     * @param timeoutSeconds Timeout em segundos
     * @return WebDriverWait configurado
     */
    public static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(timeoutSeconds)
        );
    }

    /**
     * Obtem FluentWait com polling customizado.
     *
     * @param timeoutSeconds Timeout em segundos
     * @param pollingMillis  Intervalo de polling em milissegundos
     * @return FluentWait configurado
     */
    public static FluentWait<WebDriver> getFluentWait(int timeoutSeconds, long pollingMillis) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    /**
     * Espera elemento estar visivel.
     *
     * @param locator Localizador do elemento
     * @return WebElement visivel
     */
    public static WebElement waitForVisible(By locator) {
        logger.debug("Aguardando elemento visivel: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou visivel: " + locator, e);
        }
    }

    /**
     * Espera elemento estar visivel com timeout customizado.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return WebElement visivel
     */
    public static WebElement waitForVisible(By locator, int timeoutSeconds) {
        logger.debug("Aguardando elemento visivel ({}s): {}", timeoutSeconds, locator);
        try {
            return getWait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou visivel em " + timeoutSeconds + "s: " + locator, e);
        }
    }

    /**
     * Espera elemento estar clicavel.
     *
     * @param locator Localizador do elemento
     * @return WebElement clicavel
     */
    public static WebElement waitForClickable(By locator) {
        logger.debug("Aguardando elemento clicavel: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou clicavel: " + locator, e);
        }
    }

    /**
     * Espera elemento estar clicavel com timeout customizado.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return WebElement clicavel
     */
    public static WebElement waitForClickable(By locator, int timeoutSeconds) {
        logger.debug("Aguardando elemento clicavel ({}s): {}", timeoutSeconds, locator);
        try {
            return getWait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou clicavel em " + timeoutSeconds + "s: " + locator, e);
        }
    }

    /**
     * Espera elemento estar presente no DOM.
     *
     * @param locator Localizador do elemento
     * @return WebElement presente
     */
    public static WebElement waitForPresent(By locator) {
        logger.debug("Aguardando elemento presente: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao esta presente: " + locator, e);
        }
    }

    /**
     * Espera multiplos elementos estarem presentes.
     *
     * @param locator Localizador dos elementos
     * @return Lista de WebElements
     */
    public static List<WebElement> waitForAllPresent(By locator) {
        logger.debug("Aguardando elementos presentes: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elementos nao estao presentes: " + locator, e);
        }
    }

    /**
     * Espera multiplos elementos estarem visiveis.
     *
     * @param locator Localizador dos elementos
     * @return Lista de WebElements visiveis
     */
    public static List<WebElement> waitForAllVisible(By locator) {
        logger.debug("Aguardando elementos visiveis: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elementos nao estao visiveis: " + locator, e);
        }
    }

    /**
     * Espera elemento desaparecer.
     *
     * @param locator Localizador do elemento
     * @return true quando elemento desapareceu
     */
    public static boolean waitForInvisible(By locator) {
        logger.debug("Aguardando elemento invisivel: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento ainda esta visivel: " + locator, e);
        }
    }

    /**
     * Espera elemento desaparecer com timeout customizado.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return true quando elemento desapareceu
     */
    public static boolean waitForInvisible(By locator, int timeoutSeconds) {
        logger.debug("Aguardando elemento invisivel ({}s): {}", timeoutSeconds, locator);
        try {
            return getWait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento ainda visivel apos " + timeoutSeconds + "s: " + locator, e);
        }
    }

    /**
     * Espera texto estar presente no elemento.
     *
     * @param locator Localizador do elemento
     * @param text    Texto esperado
     * @return true quando texto esta presente
     */
    public static boolean waitForTextPresent(By locator, String text) {
        logger.debug("Aguardando texto '{}' em: {}", text, locator);
        try {
            return getDefaultWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Texto '" + text + "' nao encontrado em: " + locator, e);
        }
    }

    /**
     * Espera texto no valor do input.
     *
     * @param locator Localizador do elemento
     * @param text    Texto esperado
     * @return true quando texto esta presente no value
     */
    public static boolean waitForTextInValue(By locator, String text) {
        logger.debug("Aguardando texto '{}' no value de: {}", text, locator);
        try {
            return getDefaultWait().until(ExpectedConditions.textToBePresentInElementValue(locator, text));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Texto '" + text + "' nao encontrado no value de: " + locator, e);
        }
    }

    /**
     * Espera atributo ter valor especifico.
     *
     * @param locator   Localizador do elemento
     * @param attribute Nome do atributo
     * @param value     Valor esperado
     * @return true quando atributo tem o valor
     */
    public static boolean waitForAttributeToBe(By locator, String attribute, String value) {
        logger.debug("Aguardando atributo '{}' = '{}' em: {}", attribute, value, locator);
        try {
            return getDefaultWait().until(ExpectedConditions.attributeToBe(locator, attribute, value));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException(
                    "Atributo '" + attribute + "' nao tem valor '" + value + "' em: " + locator, e);
        }
    }

    /**
     * Espera atributo conter valor.
     *
     * @param locator   Localizador do elemento
     * @param attribute Nome do atributo
     * @param value     Valor parcial esperado
     * @return true quando atributo contem o valor
     */
    public static boolean waitForAttributeContains(By locator, String attribute, String value) {
        logger.debug("Aguardando atributo '{}' conter '{}' em: {}", attribute, value, locator);
        try {
            return getDefaultWait().until(ExpectedConditions.attributeContains(locator, attribute, value));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException(
                    "Atributo '" + attribute + "' nao contem '" + value + "' em: " + locator, e);
        }
    }

    /**
     * Espera pagina carregar completamente.
     *
     * @return true quando pagina carregou
     */
    public static boolean waitForPageLoad() {
        logger.debug("Aguardando pagina carregar completamente");
        try {
            return getDefaultWait().until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return "complete".equals(js.executeScript("return document.readyState"));
            });
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Pagina nao carregou completamente", e);
        }
    }

    /**
     * Espera requisicoes AJAX completarem.
     *
     * @return true quando AJAX completou
     */
    public static boolean waitForAjax() {
        logger.debug("Aguardando requisicoes AJAX completarem");
        try {
            return getDefaultWait().until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript("return jQuery.active == 0");
            });
        } catch (TimeoutException e) {
            logger.warn("Timeout aguardando AJAX ou jQuery nao disponivel");
            return true;
        }
    }

    /**
     * Espera URL conter texto.
     *
     * @param urlPart Parte da URL esperada
     * @return true quando URL contem o texto
     */
    public static boolean waitForUrlContains(String urlPart) {
        logger.debug("Aguardando URL conter: {}", urlPart);
        try {
            return getDefaultWait().until(ExpectedConditions.urlContains(urlPart));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("URL nao contem: " + urlPart, e);
        }
    }

    /**
     * Espera URL ser igual.
     *
     * @param url URL esperada
     * @return true quando URL e igual
     */
    public static boolean waitForUrlToBe(String url) {
        logger.debug("Aguardando URL ser: {}", url);
        try {
            return getDefaultWait().until(ExpectedConditions.urlToBe(url));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("URL nao e: " + url, e);
        }
    }

    /**
     * Espera frame estar disponivel e muda para ele.
     *
     * @param locator Localizador do frame
     * @return WebDriver apos mudar para frame
     */
    public static WebDriver waitForFrameAndSwitch(By locator) {
        logger.debug("Aguardando frame e mudando: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Frame nao disponivel: " + locator, e);
        }
    }

    /**
     * Espera numero especifico de janelas/abas.
     *
     * @param numberOfWindows Numero esperado de janelas
     * @return true quando numero de janelas e igual
     */
    public static boolean waitForNumberOfWindows(int numberOfWindows) {
        logger.debug("Aguardando {} janelas", numberOfWindows);
        try {
            return getDefaultWait().until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Numero de janelas nao e: " + numberOfWindows, e);
        }
    }

    /**
     * Espera condicao customizada.
     *
     * @param condition Condicao a ser aguardada
     * @param <T>       Tipo do retorno
     * @return Resultado da condicao
     */
    public static <T> T waitForCondition(ExpectedCondition<T> condition) {
        logger.debug("Aguardando condicao customizada");
        try {
            return getDefaultWait().until(condition);
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Condicao customizada nao atendida", e);
        }
    }

    /**
     * Espera condicao customizada com timeout.
     *
     * @param condition      Condicao a ser aguardada
     * @param timeoutSeconds Timeout em segundos
     * @param <T>            Tipo do retorno
     * @return Resultado da condicao
     */
    public static <T> T waitForCondition(ExpectedCondition<T> condition, int timeoutSeconds) {
        logger.debug("Aguardando condicao customizada ({}s)", timeoutSeconds);
        try {
            return getWait(timeoutSeconds).until(condition);
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Condicao customizada nao atendida em " + timeoutSeconds + "s", e);
        }
    }

    /**
     * Espera elemento nao estar mais stale (recarregado).
     *
     * @param element WebElement a verificar
     * @return true quando elemento nao esta stale
     */
    public static boolean waitForStalenessOf(WebElement element) {
        logger.debug("Aguardando elemento nao estar stale");
        try {
            return getDefaultWait().until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento ainda esta stale", e);
        }
    }

    /**
     * Espera elemento ter numero minimo de elementos filhos.
     *
     * @param parentLocator Localizador do pai
     * @param childLocator  Localizador dos filhos
     * @param minCount      Numero minimo de filhos
     * @return Lista de elementos filhos
     */
    public static List<WebElement> waitForMinimumChildren(By parentLocator, By childLocator, int minCount) {
        logger.debug("Aguardando minimo {} filhos em: {}", minCount, parentLocator);
        try {
            return getDefaultWait().until(driver -> {
                WebElement parent = driver.findElement(parentLocator);
                List<WebElement> children = parent.findElements(childLocator);
                return children.size() >= minCount ? children : null;
            });
        } catch (TimeoutException e) {
            throw new WaitTimeoutException(
                    "Menos de " + minCount + " filhos encontrados em: " + parentLocator, e);
        }
    }

    /**
     * Verifica se elemento esta visivel sem lancar excecao.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return true se elemento esta visivel
     */
    public static boolean isElementVisible(By locator, int timeoutSeconds) {
        try {
            waitForVisible(locator, timeoutSeconds);
            return true;
        } catch (WaitTimeoutException e) {
            return false;
        }
    }

    /**
     * Verifica se elemento esta presente sem lancar excecao.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return true se elemento esta presente
     */
    public static boolean isElementPresent(By locator, int timeoutSeconds) {
        try {
            getWait(timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Aguarda elemento estar clicavel (visivel e habilitado).
     *
     * @param locator Localizador do elemento
     * @return WebElement clicavel
     */
    public static WebElement waitForClickable(By locator) {
        logger.debug("Aguardando elemento clicavel: {}", locator);
        try {
            return getDefaultWait().until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou clicavel: " + locator, e);
        }
    }

    /**
     * Aguarda elemento estar clicavel com timeout customizado.
     *
     * @param locator        Localizador do elemento
     * @param timeoutSeconds Timeout em segundos
     * @return WebElement clicavel
     */
    public static WebElement waitForClickable(By locator, int timeoutSeconds) {
        logger.debug("Aguardando elemento clicavel ({}s): {}", timeoutSeconds, locator);
        try {
            return getWait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new WaitTimeoutException("Elemento nao ficou clicavel apos " + timeoutSeconds + "s: " + locator, e);
        }
    }
}
