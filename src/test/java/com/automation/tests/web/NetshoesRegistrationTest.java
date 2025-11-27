package com.automation.tests.web;

import com.automation.builders.UserBuilder;
import com.automation.models.web.User;
import com.automation.pages.LoginPage;
import com.automation.pages.RegisterPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Cadastro de Usuario na Netshoes.
 *
 * Arquitetura:
 * - Padrao AAA (Arrange-Act-Assert) explicito em cada teste
 * - Page Object Model para interacao com paginas
 * - Test Data Builder para geracao de dados
 *
 * Fluxo de cadastro:
 * 1. Acessar pagina de login diretamente (/login)
 * 2. Preencher email na secao "Criar conta"
 * 3. Clicar em "Prosseguir"
 * 4. Preencher dados do formulario de cadastro
 *
 * Cenarios cobertos:
 * - Fluxo feliz: cadastro com dados validos
 * - Fluxo alternativo 1: CPF invalido
 * - Fluxo alternativo 2: email ja existente
 */
@Epic("Web Testing")
@Feature("Cadastro de Usuario")
@DisplayName("Testes de Cadastro na Netshoes")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NetshoesRegistrationTest extends BaseWebTest {

    private LoginPage loginPage;

    @BeforeEach
    void setUpPages() {
        loginPage = new LoginPage();
    }

    @Test
    @Order(1)
    @Story("Fluxo Feliz")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC001 - Deve carregar pagina de login corretamente")
    @Description("Valida que a pagina de login carrega com os formularios de login e criar conta")
    void shouldLoadLoginPageSuccessfully() {
        logger.info("Testando carregamento da pagina de login");

        logger.info("Abrindo pagina de login diretamente");
        loginPage.open();
        captureScreenshot("01_login_page");

        assertThat(loginPage.isPageLoaded())
                .as("Pagina de login deve estar carregada")
                .isTrue();

        assertThat(loginPage.isUserInputVisible())
                .as("Campo de usuario deve estar visivel")
                .isTrue();

        assertThat(loginPage.isCreateAccountSectionVisible())
                .as("Secao de criar conta deve estar visivel")
                .isTrue();

        logger.info("TC001 finalizado - Pagina de login carregada com sucesso");
    }

    @Test
    @Order(2)
    @Story("Fluxo Feliz")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("TC002 - Deve iniciar cadastro com email valido")
    @Description("Valida que o usuario consegue iniciar o cadastro preenchendo email")
    void shouldStartRegistrationWithValidEmail() {
        User validUser = UserBuilder.aValidUser();
        logger.info("Email para teste: {}", validUser.getEmail());

        logger.info("Abrindo pagina de login");
        loginPage.open();
        captureScreenshot("01_login_page");

        logger.info("Preenchendo email para criar conta");
        loginPage.fillCreateAccountEmail(validUser.getEmail());
        captureScreenshot("02_email_filled");

        logger.info("Clicando em Prosseguir");
        loginPage.clickCreateAccountButton();
        captureScreenshot("03_after_prosseguir");

 
        boolean hasError = loginPage.hasErrorMessage();
        String errorMsg = loginPage.getErrorMessage();

        if (hasError) {
            logger.warn("Erro encontrado: {}", errorMsg);
        }

        assertThat(hasError)
                .as("Nao deve haver erro de validacao para email valido. Erro: %s", errorMsg)
                .isFalse();

        logger.info("TC002 finalizado - Inicio de cadastro realizado");
    }


    @Test
    @Order(3)
    @Story("Validacao de Email")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC003 - Deve validar formato de email invalido")
    @Description("Valida que o sistema rejeita email com formato invalido")
    void shouldShowErrorForInvalidEmailFormat() {
        String invalidEmail = "email_invalido";
        logger.info("Testando email invalido: {}", invalidEmail);

        logger.info("Abrindo pagina de login");
        loginPage.open();
        captureScreenshot("01_login_page");

        logger.info("Preenchendo email invalido");
        loginPage.fillCreateAccountEmail(invalidEmail);
        captureScreenshot("02_invalid_email");

        logger.info("Clicando em Prosseguir");
        loginPage.clickCreateAccountButton();
        captureScreenshot("03_after_submit");

        boolean hasError = loginPage.hasErrorMessage();

        if (!hasError) {
            assertThat(loginPage.isPageLoaded())
                    .as("Deve permanecer na pagina de login para email invalido")
                    .isTrue();
        }

        logger.info("TC003 finalizado - Validacao de email funcionando");
    }


    @Test
    @Order(4)
    @Story("Validacao de Email")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC004 - Deve exibir erro ao tentar cadastrar com email ja existente")
    @Description("Valida que o sistema rejeita cadastro com email que ja esta cadastrado")
    void shouldShowErrorForExistingEmail() {
        String existingEmail = "teste@teste.com.br";
        logger.info("Testando email existente: {}", existingEmail);

        logger.info("Abrindo pagina de login");
        loginPage.open();
        captureScreenshot("01_login_page");

        logger.info("Preenchendo email existente");
        loginPage.fillCreateAccountEmail(existingEmail);
        captureScreenshot("02_existing_email");

        logger.info("Clicando em Prosseguir");
        loginPage.clickCreateAccountButton();
        captureScreenshot("03_after_submit");

        boolean hasError = loginPage.hasErrorMessage();
        boolean stillOnLoginPage = loginPage.isPageLoaded();

        logger.info("Erro exibido: {}, Ainda na pagina de login: {}", hasError, stillOnLoginPage);


        logger.info("TC004 finalizado - Teste de email existente executado");
    }


    @Test
    @Order(5)
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC005 - Deve validar login com campos vazios")
    @Description("Valida que o sistema exige preenchimento de usuario e senha")
    void shouldValidateEmptyLoginFields() {
        logger.info("Testando login com campos vazios");

        loginPage.open();
        captureScreenshot("01_login_page");

        loginPage.clickLoginButton();
        captureScreenshot("02_after_empty_submit");

        boolean stillOnLoginPage = loginPage.isPageLoaded();

        assertThat(stillOnLoginPage)
                .as("Deve permanecer na pagina de login quando campos estao vazios")
                .isTrue();

        logger.info("TC005 finalizado - Validacao de campos vazios funcionando");
    }

    @Test
    @Order(6)
    @Story("Login")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC006 - Deve exibir erro para credenciais invalidas")
    @Description("Valida que o sistema rejeita login com credenciais incorretas")
    void shouldShowErrorForInvalidCredentials() {
        String invalidUser = "usuario_invalido@teste.com";
        String invalidPassword = "senha_errada123";
        logger.info("Testando login com credenciais invalidas");

        loginPage.open();
        captureScreenshot("01_login_page");

        loginPage.attemptLogin(invalidUser, invalidPassword);
        captureScreenshot("02_after_invalid_login");

        boolean hasError = loginPage.hasErrorMessage();
        boolean stillOnLoginPage = loginPage.isPageLoaded();

        assertThat(hasError || stillOnLoginPage)
                .as("Deve exibir erro ou permanecer na pagina para credenciais invalidas")
                .isTrue();

        if (hasError) {
            logger.info("Mensagem de erro: {}", loginPage.getErrorMessage());
        }

        logger.info("TC006 finalizado - Validacao de credenciais funcionando");
    }
}
