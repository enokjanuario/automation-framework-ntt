package com.automation.pages;

import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para a pagina inicial da Netshoes.
 */
public class HomePage extends BasePage {

    private static final By LOGO = By.cssSelector(
            "a.logo, a[class*='logo'], img[alt*='Netshoes']"
    );

    private static final By SEARCH_INPUT = By.cssSelector("input#search, input.search__input");
    private static final By SEARCH_BUTTON = By.cssSelector(
            "button.search__button, button[aria-label*='buscar']"
    );

    private static final By USER_AREA = By.cssSelector("div.user");
    private static final By USER_LABEL = By.cssSelector("span.user__label");
    private static final By LOGIN_LINK = By.cssSelector(
            "a.user__box__link[href='/login'], a[href='/login'], div.user a[href*='login']"
    );
    private static final By USER_BOX = By.cssSelector("div.user__box");

    private static final By CART_ICON = By.cssSelector(
            "a.mini-cart__link, a[href='/cart'], a[href*='cart']"
    );
    private static final By CART_COUNT = By.cssSelector("span.mini-cart__number");

    private static final By WISHLIST_LINK = By.cssSelector("a.wishlist, a[href='/wishlist']");

    private static final By REGISTER_LINK = By.cssSelector(
            "a[href*='cadastro'], a[href*='register'], a.user__box__link--register"
    );

    private static final By COOKIE_ACCEPT_BUTTON = By.cssSelector(
            "#onetrust-accept-btn-handler, " +
            "button[id*='onetrust-accept'], " +
            "[class*='cookie'] button[class*='accept'], " +
            "button[class*='cookie-accept']"
    );

    private static final By COOKIE_BANNER = By.cssSelector(
            "#onetrust-banner-sdk, [class*='onetrust'], " +
            "[class*='cookie-banner'], [class*='cookie-consent']"
    );

    private static final By MENU_ITEMS = By.cssSelector("nav a, header nav a");
    private static final By DEPARTMENTS_MENU = By.cssSelector("[class*='department'], [class*='categoria']");

    private static final By FEATURED_PRODUCTS = By.cssSelector(
            "[class*='product-card'], article[class*='product'], [data-testid*='product']"
    );

    public HomePage() {
        super();
    }

    /**
     * Abre a pagina inicial.
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Abrir pagina inicial da Netshoes")
    public HomePage open() {
        logger.info("Abrindo pagina inicial");
        navigateToBaseUrl();
        handleCookieBanner();
        WaitUtils.waitForPageLoad();
        return this;
    }

    /**
     * Trata o banner de cookies se presente.
     */
    @Step("Aceitar cookies se banner estiver visivel")
    public void handleCookieBanner() {
        try {
            List<By> cookieSelectors = List.of(
                By.id("onetrust-accept-btn-handler"),
                By.cssSelector("button[id*='accept']"),
                By.cssSelector("[class*='cookie'] button"),
                By.cssSelector("button[class*='accept']"),
                COOKIE_ACCEPT_BUTTON
            );

            for (By selector : cookieSelectors) {
                try {
                    List<WebElement> buttons = driver.findElements(selector);
                    if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                        buttons.get(0).click();
                        logger.info("Banner de cookies aceito usando: {}", selector);
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
            logger.debug("Banner de cookies nao encontrado ou ja aceito");
        } catch (Exception e) {
            logger.debug("Erro ao tratar banner de cookies: {}", e.getMessage());
        }
    }

    /**
     * Realiza busca por produto.
     *
     * @param searchTerm Termo de busca
     * @return SearchResultsPage
     */
    @Step("Buscar produto: {searchTerm}")
    public SearchResultsPage searchProduct(String searchTerm) {
        logger.info("Buscando produto: {}", searchTerm);

        WebElement searchInput = waitForVisible(SEARCH_INPUT);
        searchInput.clear();
        searchInput.sendKeys(searchTerm);

        try {
            WebElement searchButton = driver.findElement(SEARCH_BUTTON);
            if (searchButton.isDisplayed() && searchButton.isEnabled()) {
                searchButton.click();
            } else {
                searchInput.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            logger.debug("Botao de busca nao encontrado, usando Enter");
            searchInput.sendKeys(Keys.ENTER);
        }

        return new SearchResultsPage();
    }

    /**
     * Realiza busca e aguarda resultados.
     *
     * @param searchTerm Termo de busca
     * @return SearchResultsPage
     */
    @Step("Buscar e aguardar resultados: {searchTerm}")
    public SearchResultsPage searchAndWaitResults(String searchTerm) {
        SearchResultsPage resultsPage = searchProduct(searchTerm);
        resultsPage.waitForResultsToLoad();
        return resultsPage;
    }

    /**
     * Clica no link de login.
     *
     * @return LoginPage
     */
    @Step("Clicar em login")
    public LoginPage clickLogin() {
        logger.info("Clicando em login");

        try {
            WebElement userArea = waitForVisible(USER_AREA);
            new org.openqa.selenium.interactions.Actions(driver)
                    .moveToElement(userArea)
                    .pause(java.time.Duration.ofMillis(500))
                    .perform();
            logger.debug("Hover realizado na area do usuario");
        } catch (Exception e) {
            logger.debug("Nao foi possivel fazer hover na area do usuario: {}", e.getMessage());
        }

        click(LOGIN_LINK);

        return new LoginPage();
    }

    /**
     * Clica no link de cadastro.
     *
     * @return RegisterPage
     */
    @Step("Clicar em cadastro")
    public RegisterPage clickRegister() {
        logger.info("Clicando em cadastro");

        try {
            WebElement userArea = waitForVisible(USER_AREA);
            new org.openqa.selenium.interactions.Actions(driver)
                    .moveToElement(userArea)
                    .pause(java.time.Duration.ofMillis(500))
                    .perform();
        } catch (Exception e) {
            logger.debug("Nao foi possivel fazer hover na area do usuario");
        }

        try {
            if (isVisible(REGISTER_LINK)) {
                click(REGISTER_LINK);
                return new RegisterPage();
            }
        } catch (Exception e) {
            logger.debug("Link de cadastro nao encontrado, indo via login");
        }

        clickLogin();
        LoginPage loginPage = new LoginPage();
        return new RegisterPage();
    }

    /**
     * Abre o carrinho de compras.
     *
     * @return CartPage
     */
    @Step("Abrir carrinho")
    public CartPage openCart() {
        logger.info("Abrindo carrinho");
        click(CART_ICON);
        return new CartPage();
    }

    /**
     * Obtem a quantidade de itens no carrinho.
     *
     * @return Quantidade de itens
     */
    @Step("Obter quantidade de itens no carrinho")
    public int getCartItemCount() {
        try {
            WebElement cartCount = driver.findElement(CART_COUNT);
            if (cartCount.isDisplayed()) {
                String countText = cartCount.getText().trim();
                if (!countText.isEmpty()) {
                    return Integer.parseInt(countText.replaceAll("\\D", ""));
                }
            }
        } catch (Exception e) {
            logger.debug("Contador do carrinho nao encontrado ou vazio");
        }
        return 0;
    }

    /**
     * Verifica se o campo de busca esta visivel.
     *
     * @return true se visivel
     */
    public boolean isSearchVisible() {
        try {
            List<WebElement> searchInputs = driver.findElements(SEARCH_INPUT);
            return !searchInputs.isEmpty() && searchInputs.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se ha produtos em destaque na pagina.
     *
     * @return true se ha produtos
     */
    public boolean hasFeaturedProducts() {
        try {
            return !driver.findElements(FEATURED_PRODUCTS).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se a area do usuario esta visivel.
     *
     * @return true se visivel
     */
    public boolean isUserAreaVisible() {
        try {
            List<WebElement> userAreas = driver.findElements(USER_AREA);
            return !userAreas.isEmpty() && userAreas.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            boolean searchVisible = isSearchVisible();
            boolean logoVisible = !driver.findElements(LOGO).isEmpty();
            return searchVisible || logoVisible;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Home Page";
    }
}
