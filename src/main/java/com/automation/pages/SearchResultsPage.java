package com.automation.pages;

import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object para a pagina de Resultados de Busca da Netshoes.
 */
public class SearchResultsPage extends BasePage {

    private static final By PRODUCT_LINKS = By.cssSelector("a[href*='/p/']");

    private static final By PRODUCT_LIST = By.cssSelector(
            "[class*='product-list'], [class*='ProductList'], [class*='produtos'], " +
            "[class*='results'], [class*='grid'], [class*='listing'], main"
    );

    private static final By PAGINATION = By.cssSelector(
            "[class*='pagination'], [class*='Pagination'], nav[aria-label*='pag']"
    );
    private static final By NEXT_PAGE_BUTTON = By.cssSelector(
            "[class*='next'], a[rel='next'], button[aria-label*='proxima'], " +
            "[class*='pagination'] a:last-child"
    );

    private static final By SORT_DROPDOWN = By.cssSelector(
            "select[class*='sort'], select[class*='order'], [class*='sort'] select"
    );

    private static final By NO_RESULTS_MESSAGE = By.cssSelector(
            "[class*='no-results'], [class*='NoResults'], [class*='sem-resultado'], " +
            "[class*='empty'], [class*='not-found']"
    );

    private static final By LOADING_INDICATOR = By.cssSelector(
            "[class*='loading'], [class*='Loading'], [class*='spinner']"
    );

    private List<WebElement> cachedProducts = null;

    public SearchResultsPage() {
        super();
    }

    /**
     * Aguarda resultados carregarem.
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Aguardar resultados carregarem")
    public SearchResultsPage waitForResultsToLoad() {
        logger.info("Aguardando resultados de busca");
        WaitUtils.waitForPageLoad();
        cachedProducts = null;

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_INDICATOR));
        } catch (Exception e) {
            logger.debug("Loading indicator nao encontrado ou ja desapareceu");
        }

        return this;
    }

    private List<WebElement> findProductElements() {
        if (cachedProducts != null) {
            return cachedProducts;
        }

        List<WebElement> products = driver.findElements(PRODUCT_LINKS);
        List<WebElement> visibleProducts = new ArrayList<>();

        for (WebElement product : products) {
            try {
                if (product.isDisplayed()) {
                    visibleProducts.add(product);
                }
            } catch (Exception ignored) {
            }
        }

        cachedProducts = visibleProducts;
        return visibleProducts;
    }

    /**
     * Obtem quantidade de produtos encontrados.
     *
     * @return Numero de produtos visiveis
     */
    @Step("Obter quantidade de produtos")
    public int getProductCount() {
        try {
            List<WebElement> products = findProductElements();
            logger.info("Encontrados {} produtos", products.size());
            return products.size();
        } catch (Exception e) {
            logger.warn("Erro ao contar produtos: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Verifica se ha resultados.
     *
     * @return true se ha produtos
     */
    @Step("Verificar se ha resultados")
    public boolean hasResults() {
        int count = getProductCount();
        if (count > 0) {
            return true;
        }

        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/busca") || currentUrl.contains("q=") ||
               currentUrl.contains("/search") || currentUrl.contains("query=");
    }

    /**
     * Verifica se mensagem de nenhum resultado esta visivel.
     *
     * @return true se nao ha resultados
     */
    @Step("Verificar se nao ha resultados")
    public boolean hasNoResultsMessage() {
        try {
            List<WebElement> noResultsElements = driver.findElements(NO_RESULTS_MESSAGE);
            for (WebElement element : noResultsElements) {
                if (element.isDisplayed()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return !hasResults();
        }
    }

    /**
     * Obtem nomes dos produtos visiveis.
     *
     * @return Lista de nomes
     */
    @Step("Obter nomes dos produtos")
    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        try {
            List<WebElement> products = findProductElements();
            for (WebElement product : products) {
                String text = product.getText();
                if (text != null && !text.isEmpty()) {
                    names.add(text.split("\n")[0]);
                }
            }
        } catch (Exception e) {
            logger.warn("Erro ao obter nomes dos produtos: {}", e.getMessage());
        }
        return names;
    }

    /**
     * Clica no primeiro produto da lista.
     *
     * @return ProductDetailPage
     */
    @Step("Clicar no primeiro produto")
    public ProductDetailPage clickFirstProduct() {
        logger.info("Clicando no primeiro produto");
        List<WebElement> products = findProductElements();
        if (!products.isEmpty()) {
            WebElement firstProduct = products.get(0);
            scrollToElement(firstProduct);
            firstProduct.click();
        } else {
            throw new RuntimeException("Nenhum produto encontrado para clicar");
        }
        return new ProductDetailPage();
    }

    private void scrollToElement(WebElement element) {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
        } catch (Exception e) {
            logger.debug("Erro ao fazer scroll: {}", e.getMessage());
        }
    }

    /**
     * Clica em um produto pelo indice.
     *
     * @param index Indice do produto (base 0)
     * @return ProductDetailPage
     */
    @Step("Clicar no produto de indice: {index}")
    public ProductDetailPage clickProductByIndex(int index) {
        logger.info("Clicando no produto {}", index);
        List<WebElement> products = findProductElements();
        if (index < products.size()) {
            WebElement product = products.get(index);
            scrollToElement(product);
            product.click();
        } else {
            throw new RuntimeException("Indice de produto invalido: " + index);
        }
        return new ProductDetailPage();
    }

    /**
     * Clica em produto que contenha texto no nome.
     *
     * @param partialName Parte do nome do produto
     * @return ProductDetailPage
     */
    @Step("Clicar no produto com nome contendo: {partialName}")
    public ProductDetailPage clickProductByName(String partialName) {
        logger.info("Buscando produto com nome: {}", partialName);
        List<WebElement> products = findProductElements();
        for (WebElement product : products) {
            String text = product.getText().toLowerCase();
            if (text.contains(partialName.toLowerCase())) {
                scrollToElement(product);
                product.click();
                return new ProductDetailPage();
            }
        }
        throw new RuntimeException("Produto nao encontrado: " + partialName);
    }

    /**
     * Aplica filtro de tamanho.
     *
     * @param size Tamanho desejado
     * @return Esta pagina
     */
    @Step("Filtrar por tamanho: {size}")
    public SearchResultsPage filterBySize(String size) {
        logger.info("Filtrando por tamanho: {}", size);
        cachedProducts = null;
        try {
            By sizeOption = By.xpath(String.format(
                    "//*[contains(@class,'filter') or contains(@class,'Filter')]//*[contains(text(),'%s')] | " +
                    "//label[contains(text(),'%s')] | //button[contains(text(),'%s')]",
                    size, size, size
            ));
            List<WebElement> options = driver.findElements(sizeOption);
            for (WebElement option : options) {
                if (option.isDisplayed()) {
                    option.click();
                    waitForResultsToLoad();
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("Filtro de tamanho nao disponivel: {}", e.getMessage());
        }
        return this;
    }

    /**
     * Aplica filtro de marca.
     *
     * @param brand Marca desejada
     * @return Esta pagina
     */
    @Step("Filtrar por marca: {brand}")
    public SearchResultsPage filterByBrand(String brand) {
        logger.info("Filtrando por marca: {}", brand);
        cachedProducts = null;
        try {
            By brandOption = By.xpath(String.format(
                    "//*[contains(@class,'filter') or contains(@class,'Filter')]//*[contains(text(),'%s')] | " +
                    "//label[contains(text(),'%s')] | //button[contains(text(),'%s')]",
                    brand, brand, brand
            ));
            List<WebElement> options = driver.findElements(brandOption);
            for (WebElement option : options) {
                if (option.isDisplayed()) {
                    option.click();
                    waitForResultsToLoad();
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("Filtro de marca nao disponivel: {}", e.getMessage());
        }
        return this;
    }

    /**
     * Ordena resultados por criterio.
     *
     * @param sortOption Opcao de ordenacao (ex: "Menor Preco", "Maior Preco")
     * @return Esta pagina
     */
    @Step("Ordenar por: {sortOption}")
    public SearchResultsPage sortBy(String sortOption) {
        logger.info("Ordenando por: {}", sortOption);
        cachedProducts = null;
        try {
            List<WebElement> selects = driver.findElements(SORT_DROPDOWN);
            for (WebElement select : selects) {
                if (select.isDisplayed()) {
                    new org.openqa.selenium.support.ui.Select(select).selectByVisibleText(sortOption);
                    waitForResultsToLoad();
                    return this;
                }
            }
            logger.warn("Dropdown de ordenacao nao encontrado");
        } catch (Exception e) {
            logger.warn("Opcao de ordenacao nao disponivel: {}", e.getMessage());
        }
        return this;
    }

    /**
     * Navega para proxima pagina de resultados.
     *
     * @return Esta pagina
     */
    @Step("Ir para proxima pagina")
    public SearchResultsPage nextPage() {
        logger.info("Navegando para proxima pagina");
        cachedProducts = null;
        try {
            List<WebElement> nextButtons = driver.findElements(NEXT_PAGE_BUTTON);
            for (WebElement button : nextButtons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    scrollToElement(button);
                    button.click();
                    waitForResultsToLoad();
                    return this;
                }
            }
            logger.warn("Botao de proxima pagina nao disponivel");
        } catch (Exception e) {
            logger.warn("Erro ao navegar para proxima pagina: {}", e.getMessage());
        }
        return this;
    }

    /**
     * Verifica se ha proxima pagina.
     *
     * @return true se ha proxima pagina
     */
    public boolean hasNextPage() {
        try {
            List<WebElement> nextButtons = driver.findElements(NEXT_PAGE_BUTTON);
            for (WebElement button : nextButtons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            String url = driver.getCurrentUrl();
            if (url.contains("/busca") || url.contains("q=") ||
                url.contains("/search") || url.contains("query=")) {
                return true;
            }
            return !findProductElements().isEmpty() ||
                   !driver.findElements(NO_RESULTS_MESSAGE).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Search Results Page";
    }
}
