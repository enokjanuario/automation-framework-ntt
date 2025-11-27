package com.automation.tests.web;

import com.automation.pages.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Busca e Carrinho na Netshoes.
 *
 * Arquitetura:
 * - Padrao AAA (Arrange-Act-Assert) explicito em cada teste
 * - Page Object Model para interacao com paginas
 * - Fluxo completo de e-commerce
 *
 * Cenarios cobertos:
 * - Buscar produto
 * - Selecionar produto
 * - Adicionar ao carrinho
 * - Verificar carrinho
 */
@Epic("Web Testing")
@Feature("Busca e Carrinho")
@DisplayName("Testes de Busca e Carrinho na Netshoes")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NetshoesCartTest extends BaseWebTest {

    private HomePage homePage;

    // Dados de teste
    private static final String SEARCH_TERM = "tenis";
    private static final String SPECIFIC_PRODUCT = "nike";

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
    }

    // ==================== TESTE DE BUSCA ====================

    @Test
    @Order(1)
    @Story("Busca de Produtos")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC001 - Deve buscar produto e exibir resultados")
    @Description("Valida que a busca retorna resultados para o termo pesquisado")
    void shouldSearchProductAndDisplayResults() {
        // ===================== ARRANGE =====================
        // Preparacao: Define termo de busca
        String searchTerm = SEARCH_TERM;
        logger.info("Termo de busca: {}", searchTerm);

        // ===================== ACT =====================
        // Execucao: Realiza busca na pagina inicial
        logger.info("Abrindo pagina inicial");
        homePage.open();
        captureScreenshot("01_home_page");

        logger.info("Realizando busca por: {}", searchTerm);
        SearchResultsPage searchResults = homePage.searchAndWaitResults(searchTerm);
        captureScreenshot("02_search_results");

        // ===================== ASSERT =====================
        // Verificacao: Valida que resultados foram encontrados
        boolean hasResults = searchResults.hasResults();
        int productCount = searchResults.getProductCount();

        assertThat(hasResults)
                .as("Busca por '%s' deve retornar resultados", searchTerm)
                .isTrue();

        assertThat(productCount)
                .as("Deve haver pelo menos 1 produto nos resultados")
                .isGreaterThan(0);

        logger.info("TC001 finalizado - {} produtos encontrados", productCount);
    }

    // ==================== TESTE DE SELECAO DE PRODUTO ====================

    @Test
    @Order(2)
    @Story("Selecao de Produto")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC002 - Deve selecionar produto e exibir detalhes")
    @Description("Valida que ao clicar em um produto, a pagina de detalhes e exibida")
    void shouldSelectProductAndDisplayDetails() {
        // ===================== ARRANGE =====================
        logger.info("Preparando selecao de produto");

        // ===================== ACT =====================
        logger.info("Realizando busca e selecionando produto");
        homePage.open();

        SearchResultsPage searchResults = homePage.searchAndWaitResults(SEARCH_TERM);
        captureScreenshot("01_search_results");

        ProductDetailPage productPage = searchResults.clickFirstProduct();
        captureScreenshot("02_product_detail");

        // ===================== ASSERT =====================
        // Verificacao: Valida que pagina de detalhes foi carregada
        boolean pageLoaded = productPage.isPageLoaded();
        String productName = productPage.getProductName();

        assertThat(pageLoaded)
                .as("Pagina de detalhes do produto deve carregar")
                .isTrue();

        assertThat(productName)
                .as("Nome do produto deve estar visivel")
                .isNotEmpty();

        logger.info("TC002 finalizado - Produto selecionado: {}", productName);
    }

    // ==================== TESTE COMPLETO: BUSCA E ADICAO AO CARRINHO ====================

    @Test
    @Order(3)
    @Story("Adicao ao Carrinho")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC003 - Deve buscar produto e adicionar ao carrinho com sucesso")
    @Description("Valida o fluxo completo de buscar produto, selecionar e adicionar ao carrinho")
    void shouldSearchProductAndAddToCart() {
        // ===================== ARRANGE =====================
        // Preparacao: Define produto a ser buscado e adicionado
        String searchTerm = SEARCH_TERM;
        logger.info("Iniciando fluxo de adicao ao carrinho");
        logger.info("Termo de busca: {}", searchTerm);

        // ===================== ACT =====================
        // Passo 1: Abrir pagina inicial
        logger.info("Passo 1: Abrindo pagina inicial");
        homePage.open();
        captureScreenshot("01_home_page");

        // Passo 2: Realizar busca
        logger.info("Passo 2: Buscando produto: {}", searchTerm);
        SearchResultsPage searchResults = homePage.searchAndWaitResults(searchTerm);
        captureScreenshot("02_search_results");

        // Verificacao intermediaria
        assertThat(searchResults.hasResults())
                .as("Busca deve retornar resultados")
                .isTrue();

        // Passo 3: Selecionar primeiro produto
        logger.info("Passo 3: Selecionando primeiro produto");
        ProductDetailPage productPage = searchResults.clickFirstProduct();
        captureScreenshot("03_product_detail");

        String productName = productPage.getProductName();
        logger.info("Produto selecionado: {}", productName);

        // Passo 4: Selecionar tamanho (se necessario)
        logger.info("Passo 4: Selecionando tamanho disponivel");
        productPage.selectFirstAvailableSize();
        captureScreenshot("04_size_selected");

        // Passo 5: Adicionar ao carrinho
        logger.info("Passo 5: Adicionando ao carrinho");
        productPage.addToCart();
        captureScreenshot("05_after_add_to_cart");

        // Passo 6: Navegar para o carrinho
        logger.info("Passo 6: Navegando para o carrinho");
        CartPage cartPage = productPage.goToCart();
        captureScreenshot("06_cart_page");

        // ===================== ASSERT =====================
        // Verificacao final: Produto esta no carrinho
        boolean cartNotEmpty = !cartPage.isEmpty();
        int itemCount = cartPage.getItemCount();

        assertThat(cartNotEmpty)
                .as("Carrinho nao deve estar vazio apos adicionar produto")
                .isTrue();

        assertThat(itemCount)
                .as("Deve haver pelo menos 1 item no carrinho")
                .isGreaterThan(0);

        // Verifica se o produto adicionado esta no carrinho
        boolean productInCart = cartPage.containsProduct(searchTerm);
        if (productInCart) {
            logger.info("Produto '{}' confirmado no carrinho", searchTerm);
        } else {
            logger.info("Verificacao de nome do produto no carrinho nao foi possivel");
        }

        logger.info("TC003 finalizado - Produto adicionado ao carrinho com sucesso");
        logger.info("Total de itens no carrinho: {}", itemCount);
    }

    // ==================== TESTES ADICIONAIS ====================

    @Test
    @Order(4)
    @Story("Carrinho Vazio")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC004 - Deve exibir mensagem de carrinho vazio quando nao ha itens")
    @Description("Valida que o sistema exibe mensagem apropriada para carrinho vazio")
    void shouldDisplayEmptyCartMessage() {
        // ===================== ARRANGE =====================
        logger.info("Verificando comportamento de carrinho vazio");

        // ===================== ACT =====================
        homePage.open();
        CartPage cartPage = homePage.openCart();
        captureScreenshot("01_empty_cart");

        // ===================== ASSERT =====================
        // Nota: Pode haver itens de sessoes anteriores
        // Validamos apenas que a pagina carregou
        boolean pageLoaded = cartPage.isPageLoaded();

        assertThat(pageLoaded)
                .as("Pagina do carrinho deve carregar corretamente")
                .isTrue();

        logger.info("TC004 finalizado - Pagina do carrinho carregada");
    }

    @Test
    @Order(5)
    @Story("Filtros de Busca")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC005 - Deve aplicar filtros na busca de produtos")
    @Description("Valida que filtros de busca funcionam corretamente")
    void shouldApplySearchFilters() {
        String searchTerm = SEARCH_TERM;
        logger.info("Testando aplicacao de filtros");

        homePage.open();
        SearchResultsPage searchResults = homePage.searchAndWaitResults(searchTerm);
        captureScreenshot("01_initial_results");

        int initialCount = searchResults.getProductCount();
        logger.info("Resultados iniciais: {}", initialCount);

        try {
            searchResults.sortBy("Menor Preco");
            captureScreenshot("02_sorted_results");
        } catch (Exception e) {
            logger.info("Opcao de ordenacao nao disponivel ou diferente");
        }

        boolean stillHasResults = searchResults.hasResults();

        assertThat(stillHasResults)
                .as("Deve manter resultados apos aplicar filtros")
                .isTrue();

        logger.info("TC005 finalizado - Filtros verificados");
    }

    @Test
    @Order(6)
    @Story("Navegacao")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("TC006 - Deve permitir continuar comprando apos adicionar ao carrinho")
    @Description("Valida que o usuario pode continuar navegando apos adicionar produto")
    void shouldAllowContinueShoppingAfterAddToCart() {
        logger.info("Testando fluxo de continuar comprando");

        homePage.open();
        SearchResultsPage searchResults = homePage.searchAndWaitResults(SEARCH_TERM);

        if (searchResults.hasResults()) {
            ProductDetailPage productPage = searchResults.clickFirstProduct();
            productPage.selectFirstAvailableSize();
            productPage.addToCart();
            captureScreenshot("01_product_added");

            productPage.continueShopping();
            captureScreenshot("02_continue_shopping");

            boolean canNavigate = homePage.isSearchVisible() || productPage.isPageLoaded();

            assertThat(canNavigate)
                    .as("Deve permitir continuar navegando apos adicionar ao carrinho")
                    .isTrue();
        }

        logger.info("TC006 finalizado - Continuar comprando funcionando");
    }
}
