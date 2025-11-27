package com.automation.pages;

import com.automation.models.web.User;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object para a pagina de Cadastro da Netshoes.
 */
public class RegisterPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.cssSelector(
            "input[name*='firstName'], input[name*='nome'], input[id*='firstName'], input[id*='nome'], input[placeholder*='Nome']"
    );
    private static final By LAST_NAME_INPUT = By.cssSelector(
            "input[name*='lastName'], input[name*='sobrenome'], input[id*='lastName'], input[id*='sobrenome'], input[placeholder*='Sobrenome']"
    );
    private static final By EMAIL_INPUT = By.cssSelector(
            "input[type='email'], input[name*='email'], input[id*='email'], input[placeholder*='E-mail']"
    );
    private static final By CPF_INPUT = By.cssSelector(
            "input[name*='cpf'], input[id*='cpf'], input[placeholder*='CPF'], input[mask*='cpf']"
    );
    private static final By PHONE_INPUT = By.cssSelector(
            "input[name*='phone'], input[name*='telefone'], input[id*='phone'], input[placeholder*='Telefone'], input[type='tel']"
    );
    private static final By BIRTHDATE_INPUT = By.cssSelector(
            "input[name*='birth'], input[name*='nascimento'], input[id*='birth'], input[placeholder*='Nascimento'], input[type='date']"
    );

    private static final By GENDER_MALE = By.cssSelector(
            "input[value='M'], input[value='male'], input[value='masculino'], label[for*='male'] input, label[for*='masculino'] input"
    );
    private static final By GENDER_FEMALE = By.cssSelector(
            "input[value='F'], input[value='female'], input[value='feminino'], label[for*='female'] input, label[for*='feminino'] input"
    );

    private static final By PASSWORD_INPUT = By.cssSelector(
            "input[name*='password']:not([name*='confirm']), input[id*='password']:not([id*='confirm']), input[placeholder*='Senha']:not([placeholder*='Confirmar'])"
    );
    private static final By CONFIRM_PASSWORD_INPUT = By.cssSelector(
            "input[name*='confirm'], input[name*='confirmar'], input[id*='confirmPassword'], input[placeholder*='Confirmar']"
    );

    private static final By NEWSLETTER_CHECKBOX = By.cssSelector(
            "input[name*='newsletter'], input[id*='newsletter'], input[type='checkbox'][class*='newsletter']"
    );
    private static final By TERMS_CHECKBOX = By.cssSelector(
            "input[name*='terms'], input[name*='termos'], input[id*='terms'], input[type='checkbox'][class*='terms']"
    );

    private static final By REGISTER_BUTTON = By.cssSelector(
            "button[type='submit'], button[class*='register'], button[class*='cadastrar'], input[type='submit'][value*='Cadastrar']"
    );
    private static final By CANCEL_BUTTON = By.cssSelector(
            "button[class*='cancel'], button[class*='cancelar'], a[class*='cancel']"
    );

    private static final By ERROR_MESSAGE = By.cssSelector(
            "[class*='error'], [class*='erro'], [class*='alert-error'], [role='alert'][class*='error']"
    );
    private static final By CPF_ERROR = By.cssSelector(
            "[class*='cpf'][class*='error'], [data-error='cpf'], [id*='cpf'][class*='error'], span[class*='error']"
    );
    private static final By EMAIL_ERROR = By.cssSelector(
            "[class*='email'][class*='error'], [data-error='email'], [id*='email'][class*='error']"
    );
    private static final By PASSWORD_ERROR = By.cssSelector(
            "[class*='password'][class*='error'], [data-error='password'], [id*='password'][class*='error']"
    );

    private static final By SUCCESS_MESSAGE = By.cssSelector(
            "[class*='success'], [class*='sucesso'], [class*='alert-success']"
    );

    private static final By LOADING_INDICATOR = By.cssSelector(
            "[class*='loading'], [class*='spinner'], [class*='loader']"
    );

    public RegisterPage() {
        super();
    }

    /**
     * Preenche o formulario completo com dados do User.
     *
     * @param user Dados do usuario
     * @return Esta pagina para encadeamento
     */
    @Step("Preencher formulario de cadastro completo")
    public RegisterPage fillForm(User user) {
        logger.info("Preenchendo formulario para: {}", user.getEmail());

        fillFirstName(user.getFirstName());
        fillLastName(user.getLastName());
        fillEmail(user.getEmail());
        fillCpf(user.getCpf());

        if (user.getPhone() != null) {
            fillPhone(user.getPhone());
        }
        if (user.getBirthDate() != null) {
            fillBirthDate(user.getBirthDate());
        }
        if (user.getGender() != null) {
            selectGender(user.getGender());
        }

        fillPassword(user.getPassword());
        fillConfirmPassword(user.getConfirmPassword());

        return this;
    }

    @Step("Preencher nome: {firstName}")
    public RegisterPage fillFirstName(String firstName) {
        type(FIRST_NAME_INPUT, firstName);
        return this;
    }

    @Step("Preencher sobrenome: {lastName}")
    public RegisterPage fillLastName(String lastName) {
        type(LAST_NAME_INPUT, lastName);
        return this;
    }

    @Step("Preencher email: {email}")
    public RegisterPage fillEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    @Step("Preencher CPF")
    public RegisterPage fillCpf(String cpf) {
        type(CPF_INPUT, cpf);
        return this;
    }

    @Step("Preencher telefone: {phone}")
    public RegisterPage fillPhone(String phone) {
        try {
            if (isVisible(PHONE_INPUT)) {
                type(PHONE_INPUT, phone);
            }
        } catch (Exception e) {
            logger.debug("Campo telefone nao disponivel");
        }
        return this;
    }

    @Step("Preencher data de nascimento: {birthDate}")
    public RegisterPage fillBirthDate(String birthDate) {
        try {
            if (isVisible(BIRTHDATE_INPUT)) {
                type(BIRTHDATE_INPUT, birthDate);
            }
        } catch (Exception e) {
            logger.debug("Campo nascimento nao disponivel");
        }
        return this;
    }

    @Step("Selecionar genero: {gender}")
    public RegisterPage selectGender(String gender) {
        try {
            if ("M".equalsIgnoreCase(gender) || "male".equalsIgnoreCase(gender)) {
                click(GENDER_MALE);
            } else if ("F".equalsIgnoreCase(gender) || "female".equalsIgnoreCase(gender)) {
                click(GENDER_FEMALE);
            }
        } catch (Exception e) {
            logger.debug("Campo genero nao disponivel");
        }
        return this;
    }

    @Step("Preencher senha")
    public RegisterPage fillPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    @Step("Preencher confirmacao de senha")
    public RegisterPage fillConfirmPassword(String confirmPassword) {
        type(CONFIRM_PASSWORD_INPUT, confirmPassword);
        return this;
    }

    @Step("Aceitar newsletter")
    public RegisterPage acceptNewsletter() {
        try {
            if (isVisible(NEWSLETTER_CHECKBOX) && !driver.findElement(NEWSLETTER_CHECKBOX).isSelected()) {
                click(NEWSLETTER_CHECKBOX);
            }
        } catch (Exception e) {
            logger.debug("Checkbox newsletter nao disponivel");
        }
        return this;
    }

    @Step("Aceitar termos de uso")
    public RegisterPage acceptTerms() {
        try {
            if (isVisible(TERMS_CHECKBOX) && !driver.findElement(TERMS_CHECKBOX).isSelected()) {
                click(TERMS_CHECKBOX);
            }
        } catch (Exception e) {
            logger.debug("Checkbox termos nao disponivel");
        }
        return this;
    }

    @Step("Clicar em cadastrar")
    public RegisterPage clickRegister() {
        scrollTo(REGISTER_BUTTON);
        click(REGISTER_BUTTON);
        return this;
    }

    /**
     * Realiza cadastro completo e submete.
     *
     * @param user Dados do usuario
     * @return HomePage se cadastro bem-sucedido
     */
    @Step("Realizar cadastro completo")
    public HomePage register(User user) {
        fillForm(user);
        acceptTerms();
        clickRegister();
        waitForRegistrationComplete();
        return new HomePage();
    }

    /**
     * Tenta realizar cadastro (pode falhar).
     *
     * @param user Dados do usuario
     * @return Esta pagina para verificar erros
     */
    @Step("Tentar cadastro")
    public RegisterPage attemptRegister(User user) {
        fillForm(user);
        acceptTerms();
        clickRegister();
        return this;
    }

    @Step("Verificar se ha erro geral")
    public boolean hasErrorMessage() {
        try {
            return isVisible(ERROR_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Obter mensagem de erro")
    public String getErrorMessage() {
        try {
            if (hasErrorMessage()) {
                return getText(ERROR_MESSAGE);
            }
        } catch (Exception e) {
            logger.debug("Erro ao obter mensagem: {}", e.getMessage());
        }
        return "";
    }

    @Step("Verificar erro de CPF")
    public boolean hasCpfError() {
        try {
            return isVisible(CPF_ERROR);
        } catch (Exception e) {
            return hasErrorMessage() && getErrorMessage().toLowerCase().contains("cpf");
        }
    }

    @Step("Obter mensagem de erro de CPF")
    public String getCpfErrorMessage() {
        try {
            if (isVisible(CPF_ERROR)) {
                return getText(CPF_ERROR);
            }
        } catch (Exception e) {
            logger.debug("Erro ao obter mensagem CPF");
        }
        return "";
    }

    @Step("Verificar erro de email")
    public boolean hasEmailError() {
        try {
            return isVisible(EMAIL_ERROR);
        } catch (Exception e) {
            return hasErrorMessage() && getErrorMessage().toLowerCase().contains("email");
        }
    }

    @Step("Verificar mensagem de sucesso")
    public boolean hasSuccessMessage() {
        try {
            return isVisible(SUCCESS_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForRegistrationComplete() {
        try {
            if (isVisible(LOADING_INDICATOR)) {
                waitForInvisible(LOADING_INDICATOR);
            }
            com.automation.utils.WaitUtils.waitForPageLoad();
        } catch (Exception e) {
            logger.debug("Aguardando cadastro: {}", e.getMessage());
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isVisible(EMAIL_INPUT) && isVisible(PASSWORD_INPUT);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getPageName() {
        return "Register Page";
    }
}
