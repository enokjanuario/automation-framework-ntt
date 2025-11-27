package com.automation.pages;

import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para a pagina de Detalhes do Produto da Netshoes.
 */
public class ProductDetailPage extends BasePage {

    private static final By PRODUCT_NAME = By.cssSelector(
            "h1[class*='product-name'], h1[class*='nome-produto'], [data-testid='product-name'], .product-title h1"
    );
    private static final By PRODUCT_PRICE = By.cssSelector(
            "[class*='price'][class*='current'], [class*='preco-atual'], [data-testid='product-price'], .price-current"
    );
    private static final By PRODUCT_ORIGINAL_PRICE = By.cssSelector(
            "[class*='price'][class*='original'], [class*='preco-de'], .price-original, del[class*='price']"
    );
    private static final By PRODUCT_DISCOUNT = By.cssSelector(
            "[class*='discount'], [class*='desconto'], .badge-discount"
    );
    private static final By PRODUCT_DESCRIPTION = By.cssSelector(
            "[class*='description'], [class*='descricao'], [data-testid='product-description']"
    );
    private static final By PRODUCT_IMAGE = By.cssSelector(
            "[class*='product-image'] img, [class*='gallery'] img, [data-testid='product-image']"
    );

    private static final By SIZE_OPTIONS = By.cssSelector(
            "[class*='size'] button, [class*='tamanho'] button, [data-testid='size-option'], label[class*='size']"
    );
    private static final By SIZE_SELECTED = By.cssSelector(
            "[class*='size'][class*='selected'], [class*='tamanho'][class*='active'], [class*='size'].active"
    );
    private static final By COLOR_OPTIONS = By.cssSelector(
            "[class*='color'] button, [class*='cor'] button, [data-testid='color-option'], [class*='color-option']"
    );
    private static final By QUANTITY_INPUT = By.cssSelector(
            "input[name*='quantity'], input[id*='quantity'], input[class*='quantidade']"
    );
    private static final By QUANTITY_INCREASE = By.cssSelector(
            "button[class*='increase'], button[class*='plus'], button[aria-label*='aumentar']"
    );
    private static final By QUANTITY_DECREASE = By.cssSelector(
            "button[class*='decrease'], button[class*='minus'], button[aria-label*='diminuir']"
    );

    private static final By ADD_TO_CART_BUTTON = By.cssSelector(
            "button[class*='add-to-cart'], button[class*='comprar'], button[data-testid='add-to-cart'], button[id*='buy']"
    );
    private static final By BUY_NOW_BUTTON = By.cssSelector(
            "button[class*='buy-now'], button[class*='comprar-agora'], [data-testid='buy-now']"
    );
    private static final By WISHLIST_BUTTON = By.cssSelector(
            "button[class*='wishlist'], button[class*='favorito'], [data-testid='wishlist']"
    );

    private static final By SUCCESS_MESSAGE = By.cssSelector(
            "[class*='success'], [class*='sucesso'], [class*='added-to-cart'], [data-testid='cart-success']"
    );
    private static final By ERROR_MESSAGE = By.cssSelector(
            "[class*='error'], [class*='erro'], [class*='alert-error']"
    );
    private static final By SELECT_SIZE_MESSAGE = By.cssSelector(
            "[class*='select-size'], [class*='selecione-tamanho'], [class*='warning'][class*='size']"
    );
    private static final By OUT_OF_STOCK_MESSAGE = By.cssSelector(
            "[class*='out-of-stock'], [class*='indisponivel'], [class*='sold-out']"
    );

    private static final By CART_MODAL = By.cssSelector(
            "[class*='cart-modal'], [class*='modal-carrinho'], [data-testid='cart-modal']"
    );
    private static final By CART_MODAL_CLOSE = By.cssSelector(
            "[class*='cart-modal'] [class*='close'], [class*='modal-carrinho'] button[class*='fechar']"
    );
    private static final By GO_TO_CART_BUTTON = By.cssSelector(
            "[class*='cart-modal'] a[href*='carrinho'], button[class*='ir-carrinho'], [data-testid='go-to-cart']"
    );
    private static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector(
            "[class*='cart-modal'] [class*='continue'], button[class*='continuar'], [data-testid='continue-shopping']"
    );

    private static final By LOADING_INDICATOR = By.cssSelector("[class*='loading'], [class*='spinner']");

    public ProductDetailPage() {
        super();
    }

    /**
     * Obtem nome do produto.
     *
     * @return Nome do produto
     */
    @Step("Obter nome do produto")
    public String getProductName() {
        waitForVisible(PRODUCT_NAME);
        return getText(PRODUCT_NAME);
    }

    /**
     * Obtem preco atual do produto.
     *
     * @return Preco formatado
     */
    @Step("Obter preco do produto")
    public String getProductPrice() {
        waitForVisible(PRODUCT_PRICE);
        return getText(PRODUCT_PRICE);
    }

    /**
     * Obtem preco original (se em promocao).
     *
     * @return Preco original ou vazio
     */
    @Step("Obter preco original")
    public String getOriginalPrice() {
        try {
            if (isVisible(PRODUCT_ORIGINAL_PRICE)) {
                return getText(PRODUCT_ORIGINAL_PRICE);
            }
        } catch (Exception e) {
            logger.debug("Preco original nao disponivel");
        }
        return "";
    }

    /**
     * Verifica se produto esta em promocao.
     *
     * @return true se em promocao
     */
    @Step("Verificar se produto esta em promocao")
    public boolean isOnSale() {
        try {
            return isVisible(PRODUCT_ORIGINAL_PRICE) || isVisible(PRODUCT_DISCOUNT);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se produto esta disponivel.
     *
     * @return true se disponivel
     */
    @Step("Verificar disponibilidade")
    public boolean isAvailable() {
        try {
            return !isVisible(OUT_OF_STOCK_MESSAGE) && isEnabled(ADD_TO_CART_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtem lista de tamanhos disponiveis.
     *
     * @return Lista de tamanhos
     */
    @Step("Obter tamanhos disponiveis")
    public List<String> getAvailableSizes() {
        try {
            List<WebElement> sizes = driver.findElements(SIZE_OPTIONS);
            return sizes.stream()
                    .map(WebElement::getText)
                    .filter(s -> !s.isEmpty())
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Seleciona tamanho do produto.
     *
     * @param size Tamanho desejado
     * @return Esta pagina
     */
    @Step("Selecionar tamanho: {size}")
    public ProductDetailPage selectSize(String size) {
        logger.info("Selecionando tamanho: {}", size);
        try {
            List<WebElement> sizes = driver.findElements(SIZE_OPTIONS);
            for (WebElement sizeElement : sizes) {
                if (sizeElement.getText().contains(size)) {
                    scrollTo(SIZE_OPTIONS);
                    sizeElement.click();
                    return this;
                }
            }
            By sizeByText = By.xpath(String.format(
                    "//*[contains(@class,'size')]//button[contains(text(),'%s')] | //label[contains(@class,'size') and contains(text(),'%s')]",
                    size, size
            ));
            click(sizeByText);
        } catch (Exception e) {
            logger.warn("Nao foi possivel selecionar tamanho: {}", size);
        }
        return this;
    }

    /**
     * Seleciona primeiro tamanho disponivel.
     *
     * @return Esta pagina
     */
    @Step("Selecionar primeiro tamanho disponivel")
    public ProductDetailPage selectFirstAvailableSize() {
        logger.info("Selecionando primeiro tamanho disponivel");
        try {
            List<WebElement> sizes = driver.findElements(SIZE_OPTIONS);
            for (WebElement size : sizes) {
                if (size.isEnabled()) {
                    scrollTo(SIZE_OPTIONS);
                    size.click();
                    return this;
                }
            }
        } catch (Exception e) {
            logger.debug("Tamanhos nao disponiveis ou nao requerido");
        }
        return this;
    }

    /**
     * Seleciona cor do produto.
     *
     * @param color Cor desejada
     * @return Esta pagina
     */
    @Step("Selecionar cor: {color}")
    public ProductDetailPage selectColor(String color) {
        logger.info("Selecionando cor: {}", color);
        try {
            By colorByText = By.xpath(String.format(
                    "//*[contains(@class,'color')]//*[contains(@title,'%s')] | //*[contains(@class,'cor')]//*[contains(@title,'%s')]",
                    color, color
            ));
            click(colorByText);
        } catch (Exception e) {
            logger.warn("Nao foi possivel selecionar cor: {}", color);
        }
        return this;
    }

    /**
     * Define quantidade do produto.
     *
     * @param quantity Quantidade desejada
     * @return Esta pagina
     */
    @Step("Definir quantidade: {quantity}")
    public ProductDetailPage setQuantity(int quantity) {
        logger.info("Definindo quantidade: {}", quantity);
        try {
            if (isVisible(QUANTITY_INPUT)) {
                type(QUANTITY_INPUT, String.valueOf(quantity));
            } else {
                for (int i = 1; i < quantity; i++) {
                    click(QUANTITY_INCREASE);
                }
            }
        } catch (Exception e) {
            logger.debug("Campo quantidade nao disponivel");
        }
        return this;
    }

    /**
     * Adiciona produto ao carrinho.
     *
     * @return Esta pagina
     */
    @Step("Adicionar ao carrinho")
    public ProductDetailPage addToCart() {
        logger.info("Adicionando produto ao carrinho");
        scrollTo(ADD_TO_CART_BUTTON);
        click(ADD_TO_CART_BUTTON);
        waitForAddToCartResponse();
        return this;
    }

    /**
     * Adiciona ao carrinho e vai para o carrinho.
     *
     * @return CartPage
     */
    @Step("Adicionar ao carrinho e ir para carrinho")
    public CartPage addToCartAndGoToCart() {
        addToCart();
        return goToCart();
    }

    /**
     * Navega para o carrinho apos adicionar.
     *
     * @return CartPage
     */
    @Step("Ir para o carrinho")
    public CartPage goToCart() {
        logger.info("Navegando para o carrinho");
        try {
            if (isVisible(CART_MODAL)) {
                waitForClickable(GO_TO_CART_BUTTON);
                click(GO_TO_CART_BUTTON);
            } else if (isVisible(GO_TO_CART_BUTTON)) {
                click(GO_TO_CART_BUTTON);
            } else {
                By cartIcon = By.cssSelector("a.mini-cart__link, a[href='/cart'], a[href*='cart']");
                waitForClickable(cartIcon);
                click(cartIcon);
            }
        } catch (Exception e) {
            logger.warn("Nao foi possivel clicar no botao de carrinho: {}", e.getMessage());
            throw new RuntimeException("Nao foi possivel navegar para o carrinho", e);
        }
        return new CartPage();
    }

    /**
     * Continua comprando apos adicionar ao carrinho.
     *
     * @return Esta pagina
     */
    @Step("Continuar comprando")
    public ProductDetailPage continueShopping() {
        logger.info("Continuando compras");
        try {
            if (isVisible(CONTINUE_SHOPPING_BUTTON)) {
                click(CONTINUE_SHOPPING_BUTTON);
            } else if (isVisible(CART_MODAL_CLOSE)) {
                click(CART_MODAL_CLOSE);
            }
        } catch (Exception e) {
            logger.debug("Modal de carrinho nao requer fechamento");
        }
        return this;
    }

    /**
     * Adiciona produto a lista de desejos.
     *
     * @return Esta pagina
     */
    @Step("Adicionar a lista de desejos")
    public ProductDetailPage addToWishlist() {
        logger.info("Adicionando a lista de desejos");
        try {
            click(WISHLIST_BUTTON);
        } catch (Exception e) {
            logger.warn("Botao de lista de desejos nao disponivel");
        }
        return this;
    }

    /**
     * Verifica se mensagem de sucesso esta visivel.
     *
     * @return true se produto foi adicionado
     */
    @Step("Verificar mensagem de sucesso")
    public boolean hasSuccessMessage() {
        try {
            return isVisible(SUCCESS_MESSAGE) || isVisible(CART_MODAL);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se ha mensagem de erro.
     *
     * @return true se ha erro
     */
    @Step("Verificar mensagem de erro")
    public boolean hasErrorMessage() {
        try {
            return isVisible(ERROR_MESSAGE) || isVisible(SELECT_SIZE_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtem mensagem de erro.
     *
     * @return Mensagem de erro
     */
    @Step("Obter mensagem de erro")
    public String getErrorMessage() {
        try {
            if (isVisible(ERROR_MESSAGE)) {
                return getText(ERROR_MESSAGE);
            }
            if (isVisible(SELECT_SIZE_MESSAGE)) {
                return getText(SELECT_SIZE_MESSAGE);
            }
        } catch (Exception e) {
            logger.debug("Erro ao obter mensagem");
        }
        return "";
    }

    private void waitForAddToCartResponse() {
        try {
            WaitUtils.waitForPageLoad();
            if (isVisible(LOADING_INDICATOR)) {
                waitForInvisible(LOADING_INDICATOR);
            }
            WaitUtils.getWait(5).until(driver ->
                    isVisible(SUCCESS_MESSAGE) || isVisible(CART_MODAL) || isVisible(ERROR_MESSAGE)
            );
        } catch (Exception e) {
            logger.debug("Aguardando resposta de adicao ao carrinho: {}", e.getMessage());
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isVisible(PRODUCT_NAME) && isVisible(ADD_TO_CART_BUTTON);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Product Detail Page";
    }
}
