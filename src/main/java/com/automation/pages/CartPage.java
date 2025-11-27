package com.automation.pages;

import com.automation.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para a pagina de Carrinho da Netshoes.
 */
public class CartPage extends BasePage {

    private static final By CART_CONTAINER = By.cssSelector(
            "[class*='cart'], [class*='carrinho'], [data-testid='cart']"
    );
    private static final By CART_ITEMS = By.cssSelector(
            "[class*='cart-item'], [class*='item-carrinho'], [data-testid='cart-item']"
    );
    private static final By EMPTY_CART_MESSAGE = By.cssSelector(
            "[class*='empty-cart'], [class*='carrinho-vazio'], [data-testid='empty-cart']"
    );

    private static final By ITEM_NAME = By.cssSelector(
            "[class*='cart-item'] [class*='name'], [class*='item-carrinho'] [class*='nome']"
    );
    private static final By ITEM_PRICE = By.cssSelector(
            "[class*='cart-item'] [class*='price'], [class*='item-carrinho'] [class*='preco']"
    );
    private static final By ITEM_QUANTITY = By.cssSelector(
            "[class*='cart-item'] input[type='number'], [class*='cart-item'] [class*='quantity'] input"
    );
    private static final By ITEM_REMOVE = By.cssSelector(
            "[class*='cart-item'] [class*='remove'], [class*='cart-item'] button[class*='delete'], [aria-label*='remover']"
    );
    private static final By ITEM_SIZE = By.cssSelector(
            "[class*='cart-item'] [class*='size'], [class*='cart-item'] [class*='tamanho']"
    );

    private static final By SUBTOTAL = By.cssSelector("[class*='subtotal'], [class*='sub-total']");
    private static final By SHIPPING_COST = By.cssSelector("[class*='shipping'], [class*='frete']");
    private static final By TOTAL = By.cssSelector(
            "[class*='cart-total'], [class*='total-carrinho'], [class*='order-total']"
    );
    private static final By DISCOUNT = By.cssSelector("[class*='discount'], [class*='desconto']");

    private static final By COUPON_INPUT = By.cssSelector(
            "input[name*='coupon'], input[name*='cupom'], input[id*='coupon'], input[placeholder*='cupom']"
    );
    private static final By COUPON_APPLY_BUTTON = By.cssSelector(
            "button[class*='coupon'], button[class*='cupom'], button[class*='apply']"
    );
    private static final By COUPON_SUCCESS = By.cssSelector(
            "[class*='coupon'][class*='success'], [class*='cupom'][class*='sucesso']"
    );
    private static final By COUPON_ERROR = By.cssSelector(
            "[class*='coupon'][class*='error'], [class*='cupom'][class*='erro']"
    );

    private static final By CEP_INPUT = By.cssSelector(
            "input[name*='cep'], input[name*='zipcode'], input[id*='cep'], input[placeholder*='CEP']"
    );
    private static final By CEP_CALCULATE_BUTTON = By.cssSelector(
            "button[class*='cep'], button[class*='calcular'], button[class*='shipping']"
    );

    private static final By CHECKOUT_BUTTON = By.cssSelector(
            "button[class*='checkout'], button[class*='finalizar'], a[href*='checkout'], [data-testid='checkout']"
    );
    private static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector(
            "a[class*='continue'], a[class*='continuar'], button[class*='continue']"
    );
    private static final By CLEAR_CART_BUTTON = By.cssSelector(
            "button[class*='clear-cart'], button[class*='limpar'], a[class*='limpar']"
    );

    private static final By LOADING_INDICATOR = By.cssSelector("[class*='loading'], [class*='spinner']");

    public CartPage() {
        super();
    }

    /**
     * Verifica se o carrinho esta vazio.
     *
     * @return true se vazio
     */
    @Step("Verificar se carrinho esta vazio")
    public boolean isEmpty() {
        try {
            return isVisible(EMPTY_CART_MESSAGE) || getItemCount() == 0;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Obtem quantidade de itens no carrinho.
     *
     * @return Numero de itens
     */
    @Step("Obter quantidade de itens no carrinho")
    public int getItemCount() {
        try {
            List<WebElement> items = driver.findElements(CART_ITEMS);
            logger.info("Carrinho contem {} itens", items.size());
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Obtem nomes dos produtos no carrinho.
     *
     * @return Lista de nomes
     */
    @Step("Obter nomes dos produtos no carrinho")
    public List<String> getItemNames() {
        try {
            List<WebElement> names = driver.findElements(ITEM_NAME);
            return names.stream().map(WebElement::getText).toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Verifica se produto especifico esta no carrinho.
     *
     * @param productName Nome ou parte do nome do produto
     * @return true se encontrado
     */
    @Step("Verificar se produto esta no carrinho: {productName}")
    public boolean containsProduct(String productName) {
        return getItemNames().stream()
                .anyMatch(name -> name.toLowerCase().contains(productName.toLowerCase()));
    }

    /**
     * Remove primeiro item do carrinho.
     *
     * @return Esta pagina
     */
    @Step("Remover primeiro item do carrinho")
    public CartPage removeFirstItem() {
        logger.info("Removendo primeiro item do carrinho");
        try {
            click(ITEM_REMOVE);
            waitForCartUpdate();
        } catch (Exception e) {
            logger.warn("Nao foi possivel remover item");
        }
        return this;
    }

    /**
     * Remove item pelo indice.
     *
     * @param index Indice do item (base 0)
     * @return Esta pagina
     */
    @Step("Remover item de indice: {index}")
    public CartPage removeItemByIndex(int index) {
        logger.info("Removendo item de indice: {}", index);
        try {
            List<WebElement> removeButtons = driver.findElements(ITEM_REMOVE);
            if (index < removeButtons.size()) {
                removeButtons.get(index).click();
                waitForCartUpdate();
            }
        } catch (Exception e) {
            logger.warn("Nao foi possivel remover item de indice: {}", index);
        }
        return this;
    }

    /**
     * Limpa o carrinho (remove todos os itens).
     *
     * @return Esta pagina
     */
    @Step("Limpar carrinho")
    public CartPage clearCart() {
        logger.info("Limpando carrinho");
        try {
            if (isVisible(CLEAR_CART_BUTTON)) {
                click(CLEAR_CART_BUTTON);
            } else {
                while (!isEmpty()) {
                    removeFirstItem();
                }
            }
        } catch (Exception e) {
            logger.warn("Nao foi possivel limpar carrinho");
        }
        return this;
    }

    /**
     * Atualiza quantidade de um item.
     *
     * @param index    Indice do item
     * @param quantity Nova quantidade
     * @return Esta pagina
     */
    @Step("Atualizar quantidade do item {index} para {quantity}")
    public CartPage updateItemQuantity(int index, int quantity) {
        logger.info("Atualizando quantidade do item {} para {}", index, quantity);
        try {
            List<WebElement> quantityInputs = driver.findElements(ITEM_QUANTITY);
            if (index < quantityInputs.size()) {
                WebElement input = quantityInputs.get(index);
                input.clear();
                input.sendKeys(String.valueOf(quantity));
                waitForCartUpdate();
            }
        } catch (Exception e) {
            logger.warn("Nao foi possivel atualizar quantidade");
        }
        return this;
    }

    /**
     * Aplica cupom de desconto.
     *
     * @param couponCode Codigo do cupom
     * @return Esta pagina
     */
    @Step("Aplicar cupom: {couponCode}")
    public CartPage applyCoupon(String couponCode) {
        logger.info("Aplicando cupom: {}", couponCode);
        try {
            type(COUPON_INPUT, couponCode);
            click(COUPON_APPLY_BUTTON);
            waitForCartUpdate();
        } catch (Exception e) {
            logger.warn("Nao foi possivel aplicar cupom");
        }
        return this;
    }

    /**
     * Verifica se cupom foi aplicado com sucesso.
     *
     * @return true se sucesso
     */
    @Step("Verificar se cupom foi aplicado")
    public boolean isCouponApplied() {
        try {
            return isVisible(COUPON_SUCCESS) || isVisible(DISCOUNT);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se ha erro no cupom.
     *
     * @return true se ha erro
     */
    @Step("Verificar erro no cupom")
    public boolean hasCouponError() {
        try {
            return isVisible(COUPON_ERROR);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calcula frete pelo CEP.
     *
     * @param cep CEP para calculo
     * @return Esta pagina
     */
    @Step("Calcular frete para CEP: {cep}")
    public CartPage calculateShipping(String cep) {
        logger.info("Calculando frete para: {}", cep);
        try {
            type(CEP_INPUT, cep);
            click(CEP_CALCULATE_BUTTON);
            waitForCartUpdate();
        } catch (Exception e) {
            logger.warn("Nao foi possivel calcular frete");
        }
        return this;
    }

    /**
     * Obtem subtotal do carrinho.
     *
     * @return Valor do subtotal
     */
    @Step("Obter subtotal")
    public String getSubtotal() {
        try {
            return getText(SUBTOTAL);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Obtem valor do frete.
     *
     * @return Valor do frete
     */
    @Step("Obter valor do frete")
    public String getShippingCost() {
        try {
            return getText(SHIPPING_COST);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Obtem total do carrinho.
     *
     * @return Valor total
     */
    @Step("Obter total do carrinho")
    public String getTotal() {
        try {
            return getText(TOTAL);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Prossegue para checkout.
     *
     * @return Esta pagina
     */
    @Step("Prosseguir para checkout")
    public CartPage proceedToCheckout() {
        logger.info("Prosseguindo para checkout");
        scrollTo(CHECKOUT_BUTTON);
        click(CHECKOUT_BUTTON);
        WaitUtils.waitForPageLoad();
        return this;
    }

    /**
     * Continua comprando.
     *
     * @return HomePage
     */
    @Step("Continuar comprando")
    public HomePage continueShopping() {
        logger.info("Continuando compras");
        click(CONTINUE_SHOPPING_BUTTON);
        return new HomePage();
    }

    private void waitForCartUpdate() {
        try {
            if (isVisible(LOADING_INDICATOR)) {
                waitForInvisible(LOADING_INDICATOR);
            }
            WaitUtils.waitForPageLoad();
        } catch (Exception e) {
            logger.debug("Aguardando atualizacao do carrinho");
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isVisible(CART_CONTAINER) || isVisible(EMPTY_CART_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Cart Page";
    }
}
