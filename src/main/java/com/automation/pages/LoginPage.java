package com.automation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object para a pagina de Login da Netshoes.
 */
public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "https://www.netshoes.com.br/login";

    private static final By USER_INPUT = By.id("user");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector(
            "section.login__form button[data-testId='submitButton'][type='submit']"
    );
    private static final By LOGIN_WITHOUT_PASSWORD_BUTTON = By.xpath(
            "//section[contains(@class,'login__form')]//button[contains(.,'Entrar sem senha')]"
    );

    private static final By CREATE_ACCOUNT_EMAIL_INPUT = By.id("email");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector(
            "section.create-account button.buttonSubmit[data-testId='submitButton']"
    );
    private static final By CREATE_ACCOUNT_TOGGLE = By.xpath(
            "//section[contains(@class,'create-account')]//button[contains(.,'Criar conta')]"
    );

    private static final By FORGOT_PASSWORD_LINK = By.cssSelector("span.remember-password");

    private static final By FACEBOOK_LOGIN = By.cssSelector("div.facebook button.facebook__button");
    private static final By GOOGLE_LOGIN = By.id("buttonDiv");

    private static final By ERROR_MESSAGE = By.cssSelector(
            "[class*='error'], [class*='erro'], [class*='alert-error'], .base-input--error"
    );
    private static final By INPUT_ERROR = By.cssSelector(".base-input--error, .base-input__error");

    private static final By LOADING_INDICATOR = By.cssSelector(
            "[class*='loading'], [class*='spinner'], .btn--loading"
    );

    private static final By PAGE_TITLE = By.cssSelector("span.title");

    public LoginPage() {
        super();
    }

    /**
     * Abre a pagina de login diretamente.
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Abrir pagina de login")
    public LoginPage open() {
        logger.info("Abrindo pagina de login: {}", LOGIN_URL);
        navigateTo(LOGIN_URL);
        waitForPageLoad();
        return this;
    }

    /**
     * Realiza login com credenciais fornecidas.
     *
     * @param user     Email, CPF ou CNPJ do usuario
     * @param password Senha do usuario
     * @return HomePage apos login bem-sucedido
     */
    @Step("Realizar login com usuario: {user}")
    public HomePage login(String user, String password) {
        logger.info("Realizando login com: {}", user);
        fillUser(user);
        fillPassword(password);
        clickLoginButton();
        waitForLoginComplete();
        return new HomePage();
    }

    /**
     * Tenta realizar login (pode falhar).
     *
     * @param user     Email, CPF ou CNPJ do usuario
     * @param password Senha do usuario
     * @return Esta pagina para verificar erros
     */
    @Step("Tentar login com usuario: {user}")
    public LoginPage attemptLogin(String user, String password) {
        logger.info("Tentando login com: {}", user);
        fillUser(user);
        fillPassword(password);
        clickLoginButton();
        waitForErrorOrRedirect();
        return this;
    }

    /**
     * Preenche o campo de usuario (email, CPF ou CNPJ).
     *
     * @param user Usuario a preencher
     * @return Esta pagina para encadeamento
     */
    @Step("Preencher usuario: {user}")
    public LoginPage fillUser(String user) {
        waitForVisible(USER_INPUT);
        type(USER_INPUT, user);
        return this;
    }

    /**
     * Preenche o campo de senha.
     *
     * @param password Senha a preencher
     * @return Esta pagina para encadeamento
     */
    @Step("Preencher senha")
    public LoginPage fillPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Clica no botao de login "Acessar conta".
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Clicar no botao Acessar conta")
    public LoginPage clickLoginButton() {
        click(LOGIN_BUTTON);
        return this;
    }

    /**
     * Inicia o processo de criacao de conta preenchendo o email.
     *
     * @param email Email para criar conta
     * @return RegisterPage apos clicar em Prosseguir
     */
    @Step("Iniciar criacao de conta com email: {email}")
    public RegisterPage startCreateAccount(String email) {
        logger.info("Iniciando criacao de conta com email: {}", email);
        fillCreateAccountEmail(email);
        clickCreateAccountButton();
        return new RegisterPage();
    }

    /**
     * Preenche o email na secao "Criar conta".
     *
     * @param email Email a preencher
     * @return Esta pagina para encadeamento
     */
    @Step("Preencher email para criar conta: {email}")
    public LoginPage fillCreateAccountEmail(String email) {
        waitForVisible(CREATE_ACCOUNT_EMAIL_INPUT);
        type(CREATE_ACCOUNT_EMAIL_INPUT, email);
        return this;
    }

    /**
     * Clica no botao "Prosseguir" para criar conta.
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Clicar em Prosseguir")
    public LoginPage clickCreateAccountButton() {
        click(CREATE_ACCOUNT_BUTTON);
        waitForPageLoad();
        return this;
    }

    /**
     * Clica no botao toggle "Criar conta" (se estiver em mobile).
     *
     * @return Esta pagina para encadeamento
     */
    @Step("Expandir secao Criar conta")
    public LoginPage expandCreateAccount() {
        try {
            if (isVisible(CREATE_ACCOUNT_TOGGLE)) {
                click(CREATE_ACCOUNT_TOGGLE);
            }
        } catch (Exception e) {
            logger.debug("Secao criar conta ja visivel");
        }
        return this;
    }

    /**
     * Clica em "Esqueci minha senha".
     */
    @Step("Clicar em Esqueci minha senha")
    public void clickForgotPassword() {
        logger.info("Clicando em Esqueci minha senha");
        click(FORGOT_PASSWORD_LINK);
    }

    /**
     * Verifica se mensagem de erro esta visivel.
     *
     * @return true se ha mensagem de erro
     */
    @Step("Verificar se ha mensagem de erro")
    public boolean hasErrorMessage() {
        try {
            return isVisible(ERROR_MESSAGE) || isVisible(INPUT_ERROR);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtem texto da mensagem de erro.
     *
     * @return Texto da mensagem ou vazio
     */
    @Step("Obter mensagem de erro")
    public String getErrorMessage() {
        try {
            if (isVisible(ERROR_MESSAGE)) {
                return getText(ERROR_MESSAGE);
            }
            if (isVisible(INPUT_ERROR)) {
                return getText(INPUT_ERROR);
            }
        } catch (Exception e) {
            logger.debug("Erro ao obter mensagem: {}", e.getMessage());
        }
        return "";
    }

    /**
     * Verifica se o botao de login esta habilitado.
     *
     * @return true se habilitado
     */
    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }

    /**
     * Verifica se o campo de usuario esta visivel.
     *
     * @return true se visivel
     */
    public boolean isUserInputVisible() {
        try {
            return isVisible(USER_INPUT);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se a secao de criar conta esta visivel.
     *
     * @return true se visivel
     */
    public boolean isCreateAccountSectionVisible() {
        try {
            return isVisible(CREATE_ACCOUNT_EMAIL_INPUT);
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForLoginComplete() {
        try {
            if (isVisible(LOADING_INDICATOR)) {
                waitForInvisible(LOADING_INDICATOR);
            }
            com.automation.utils.WaitUtils.waitForPageLoad();
        } catch (Exception e) {
            logger.debug("Aguardando login: {}", e.getMessage());
        }
    }

    private void waitForErrorOrRedirect() {
        try {
            waitForInvisible(LOADING_INDICATOR);
        } catch (Exception e) {
            logger.debug("Aguardando resposta: {}", e.getMessage());
        }
    }

    private void waitForPageLoad() {
        try {
            waitForVisible(USER_INPUT);
        } catch (Exception e) {
            logger.debug("Aguardando pagina carregar: {}", e.getMessage());
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isVisible(USER_INPUT) && isVisible(PASSWORD_INPUT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Netshoes Login Page";
    }
}
