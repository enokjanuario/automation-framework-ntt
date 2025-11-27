package com.automation.utils;

import com.automation.core.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utilitarios para manipulacao de elementos Web.
 *
 * Arquitetura:
 * - Acoes robustas com retry automatico
 * - Tratamento de elementos dinamicos
 * - Suporte a scroll, hover, drag-and-drop
 *
 * Boas praticas:
 * - Sempre usa waits antes de interagir
 * - Trata StaleElementReferenceException
 * - Log detalhado de acoes
 */
public final class ElementUtils {

    private static final Logger logger = LoggerFactory.getLogger(ElementUtils.class);
    private static final int MAX_RETRY_COUNT = 3;

    private ElementUtils() {
        // Construtor privado para impedir instanciacao
    }

    // ==================== Acoes de Click ====================

    /**
     * Clica em elemento com retry automatico.
     *
     * @param locator Localizador do elemento
     */
    @Step("Clicar em elemento: {locator}")
    public static void click(By locator) {
        logger.info("Clicando em elemento: {}", locator);
        retryAction(() -> {
            WebElement element = WaitUtils.waitForClickable(locator);
            element.click();
        });
    }

    /**
     * Clica em elemento usando JavaScript.
     *
     * @param locator Localizador do elemento
     */
    @Step("Clicar via JavaScript: {locator}")
    public static void clickByJs(By locator) {
        logger.info("Clicando via JavaScript: {}", locator);
        WebElement element = WaitUtils.waitForPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Duplo clique em elemento.
     *
     * @param locator Localizador do elemento
     */
    @Step("Duplo clique: {locator}")
    public static void doubleClick(By locator) {
        logger.info("Duplo clique em: {}", locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        new Actions(DriverManager.getDriver()).doubleClick(element).perform();
    }

    /**
     * Clique com botao direito (context click).
     *
     * @param locator Localizador do elemento
     */
    @Step("Clique direito: {locator}")
    public static void rightClick(By locator) {
        logger.info("Clique direito em: {}", locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        new Actions(DriverManager.getDriver()).contextClick(element).perform();
    }

    // ==================== Acoes de Input ====================

    /**
     * Preenche campo de texto.
     *
     * @param locator Localizador do elemento
     * @param text    Texto a preencher
     */
    @Step("Preencher campo: {locator} com texto")
    public static void type(By locator, String text) {
        logger.info("Preenchendo campo {} com: {}", locator, text);
        retryAction(() -> {
            WebElement element = WaitUtils.waitForClickable(locator);
            element.clear();
            element.sendKeys(text);
        });
    }

    /**
     * Preenche campo de texto sem limpar conteudo anterior.
     *
     * @param locator Localizador do elemento
     * @param text    Texto a adicionar
     */
    @Step("Adicionar texto: {locator}")
    public static void append(By locator, String text) {
        logger.info("Adicionando texto em {} : {}", locator, text);
        WebElement element = WaitUtils.waitForClickable(locator);
        element.sendKeys(text);
    }

    /**
     * Limpa campo de texto.
     *
     * @param locator Localizador do elemento
     */
    @Step("Limpar campo: {locator}")
    public static void clear(By locator) {
        logger.info("Limpando campo: {}", locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        element.clear();
    }

    /**
     * Preenche campo usando JavaScript.
     *
     * @param locator Localizador do elemento
     * @param text    Texto a preencher
     */
    @Step("Preencher via JavaScript: {locator}")
    public static void typeByJs(By locator, String text) {
        logger.info("Preenchendo via JavaScript {} com: {}", locator, text);
        WebElement element = WaitUtils.waitForPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].value = arguments[1];", element, text);
    }

    /**
     * Envia tecla especial.
     *
     * @param locator Localizador do elemento
     * @param key     Tecla a enviar
     */
    @Step("Enviar tecla: {key} para {locator}")
    public static void sendKey(By locator, Keys key) {
        logger.info("Enviando tecla {} para: {}", key.name(), locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        element.sendKeys(key);
    }

    // ==================== Acoes de Select ====================

    /**
     * Seleciona opcao por texto visivel.
     *
     * @param locator Localizador do select
     * @param text    Texto da opcao
     */
    @Step("Selecionar por texto: {text}")
    public static void selectByText(By locator, String text) {
        logger.info("Selecionando '{}' em: {}", text, locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        new Select(element).selectByVisibleText(text);
    }

    /**
     * Seleciona opcao por valor.
     *
     * @param locator Localizador do select
     * @param value   Valor da opcao
     */
    @Step("Selecionar por valor: {value}")
    public static void selectByValue(By locator, String value) {
        logger.info("Selecionando valor '{}' em: {}", value, locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        new Select(element).selectByValue(value);
    }

    /**
     * Seleciona opcao por indice.
     *
     * @param locator Localizador do select
     * @param index   Indice da opcao (base 0)
     */
    @Step("Selecionar por indice: {index}")
    public static void selectByIndex(By locator, int index) {
        logger.info("Selecionando indice {} em: {}", index, locator);
        WebElement element = WaitUtils.waitForClickable(locator);
        new Select(element).selectByIndex(index);
    }

    // ==================== Acoes de Hover e Scroll ====================

    /**
     * Move o mouse sobre elemento (hover).
     *
     * @param locator Localizador do elemento
     */
    @Step("Hover sobre: {locator}")
    public static void hover(By locator) {
        logger.info("Hover sobre: {}", locator);
        WebElement element = WaitUtils.waitForVisible(locator);
        new Actions(DriverManager.getDriver()).moveToElement(element).perform();
    }

    /**
     * Faz scroll ate o elemento.
     *
     * @param locator Localizador do elemento
     */
    @Step("Scroll ate: {locator}")
    public static void scrollToElement(By locator) {
        logger.info("Scroll ate: {}", locator);
        WebElement element = WaitUtils.waitForPresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Faz scroll ate o topo da pagina.
     */
    @Step("Scroll para topo")
    public static void scrollToTop() {
        logger.info("Scroll para topo da pagina");
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Faz scroll ate o final da pagina.
     */
    @Step("Scroll para final")
    public static void scrollToBottom() {
        logger.info("Scroll para final da pagina");
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ==================== Getters ====================

    /**
     * Obtem texto do elemento.
     *
     * @param locator Localizador do elemento
     * @return Texto do elemento
     */
    @Step("Obter texto: {locator}")
    public static String getText(By locator) {
        logger.debug("Obtendo texto de: {}", locator);
        WebElement element = WaitUtils.waitForVisible(locator);
        return element.getText();
    }

    /**
     * Obtem valor do atributo.
     *
     * @param locator   Localizador do elemento
     * @param attribute Nome do atributo
     * @return Valor do atributo
     */
    @Step("Obter atributo {attribute}: {locator}")
    public static String getAttribute(By locator, String attribute) {
        logger.debug("Obtendo atributo '{}' de: {}", attribute, locator);
        WebElement element = WaitUtils.waitForPresent(locator);
        return element.getAttribute(attribute);
    }

    /**
     * Obtem valor do campo (atributo value).
     *
     * @param locator Localizador do elemento
     * @return Valor do campo
     */
    @Step("Obter valor: {locator}")
    public static String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    /**
     * Obtem lista de textos de multiplos elementos.
     *
     * @param locator Localizador dos elementos
     * @return Lista de textos
     */
    @Step("Obter textos: {locator}")
    public static List<String> getTexts(By locator) {
        logger.debug("Obtendo textos de: {}", locator);
        List<WebElement> elements = WaitUtils.waitForAllVisible(locator);
        return elements.stream().map(WebElement::getText).toList();
    }

    /**
     * Conta numero de elementos.
     *
     * @param locator Localizador dos elementos
     * @return Quantidade de elementos
     */
    @Step("Contar elementos: {locator}")
    public static int count(By locator) {
        logger.debug("Contando elementos: {}", locator);
        try {
            List<WebElement> elements = DriverManager.getDriver().findElements(locator);
            return elements.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // ==================== Verificacoes ====================

    /**
     * Verifica se elemento esta visivel.
     *
     * @param locator Localizador do elemento
     * @return true se elemento esta visivel
     */
    public static boolean isVisible(By locator) {
        try {
            WebElement element = DriverManager.getDriver().findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se elemento esta habilitado.
     *
     * @param locator Localizador do elemento
     * @return true se elemento esta habilitado
     */
    public static boolean isEnabled(By locator) {
        try {
            WebElement element = DriverManager.getDriver().findElement(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se elemento esta selecionado (checkbox/radio).
     *
     * @param locator Localizador do elemento
     * @return true se elemento esta selecionado
     */
    public static boolean isSelected(By locator) {
        try {
            WebElement element = DriverManager.getDriver().findElement(locator);
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== Metodos Auxiliares ====================

    /**
     * Executa acao com retry automatico.
     *
     * @param action Acao a executar
     */
    private static void retryAction(Runnable action) {
        int attempts = 0;
        while (attempts < MAX_RETRY_COUNT) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.warn("StaleElementReferenceException - tentativa {} de {}", attempts, MAX_RETRY_COUNT);
                if (attempts >= MAX_RETRY_COUNT) {
                    throw e;
                }
            }
        }
    }

    /**
     * Highlight elemento para debug/screenshot.
     *
     * @param locator Localizador do elemento
     */
    @Step("Destacar elemento: {locator}")
    public static void highlight(By locator) {
        WebElement element = WaitUtils.waitForVisible(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript(
                "arguments[0].style.border='3px solid red'; arguments[0].style.backgroundColor='yellow';",
                element
        );
    }
}
