package com.automation.builders;

import com.automation.models.web.User;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Builder para criacao de objetos User com dados de teste para Web.
 *
 * Arquitetura:
 * - Implementa Test Data Builder Pattern
 * - Utiliza Faker para geracao de dados brasileiros realistas
 * - Fornece metodos para cenarios especificos de teste
 * - Gera CPFs validos e invalidos para testes
 *
 * Uso:
 * User user = new UserBuilder().build();                    // User com dados aleatorios
 * User user = UserBuilder.aValidUser();                     // User valido padrao
 * User user = UserBuilder.aUserWithInvalidCpf();            // User com CPF invalido
 */
public class UserBuilder {

    private static final Faker faker = new Faker(new Locale("pt-BR"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String password;
    private String confirmPassword;
    private String phone;
    private String birthDate;
    private String gender;

    /**
     * Construtor que inicializa com valores aleatorios validos.
     */
    public UserBuilder() {
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.email = generateUniqueEmail();
        this.cpf = generateValidCpf();
        this.password = "Test@123456";
        this.confirmPassword = "Test@123456";
        this.phone = faker.phoneNumber().cellPhone().replaceAll("[^0-9]", "");
        this.birthDate = generateBirthDate();
        this.gender = faker.options().option("M", "F");
    }

    /**
     * Gera um email unico para evitar conflitos de cadastro.
     */
    private String generateUniqueEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = faker.lorem().characters(5, false, false);
        return String.format("test_%s_%s@testmail.com", timestamp, random);
    }

    /**
     * Gera uma data de nascimento para pessoa maior de idade.
     */
    private String generateBirthDate() {
        int age = faker.number().numberBetween(18, 60);
        LocalDate birthDate = LocalDate.now().minusYears(age);
        return birthDate.format(DATE_FORMAT);
    }

    /**
     * Gera um CPF valido utilizando algoritmo oficial.
     */
    private String generateValidCpf() {
        int[] cpf = new int[11];

        // Gera os 9 primeiros digitos
        for (int i = 0; i < 9; i++) {
            cpf[i] = faker.number().numberBetween(0, 9);
        }

        // Calcula primeiro digito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += cpf[i] * (10 - i);
        }
        int remainder = sum % 11;
        cpf[9] = remainder < 2 ? 0 : 11 - remainder;

        // Calcula segundo digito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += cpf[i] * (11 - i);
        }
        remainder = sum % 11;
        cpf[10] = remainder < 2 ? 0 : 11 - remainder;

        // Formata CPF
        StringBuilder sb = new StringBuilder();
        for (int digit : cpf) {
            sb.append(digit);
        }
        return sb.toString();
    }

    // ==================== Factory Methods ====================

    /**
     * Cria um User valido padrao.
     */
    public static User aValidUser() {
        return new UserBuilder().build();
    }

    /**
     * Cria um User com CPF invalido para teste de validacao.
     */
    public static User aUserWithInvalidCpf() {
        return new UserBuilder()
                .withCpf("11111111111")
                .build();
    }

    /**
     * Cria um User com email que ja existe (simulado).
     * Utilize um email conhecido que ja esteja cadastrado no sistema.
     */
    public static User aUserWithExistingEmail(String existingEmail) {
        return new UserBuilder()
                .withEmail(existingEmail)
                .build();
    }

    /**
     * Cria um User com senha fraca para teste de validacao.
     */
    public static User aUserWithWeakPassword() {
        return new UserBuilder()
                .withPassword("123")
                .withConfirmPassword("123")
                .build();
    }

    /**
     * Cria um User com senhas que nao conferem.
     */
    public static User aUserWithMismatchedPasswords() {
        return new UserBuilder()
                .withPassword("Test@123456")
                .withConfirmPassword("DifferentPassword@123")
                .build();
    }

    /**
     * Cria um User com campos obrigatorios vazios.
     */
    public static User aUserWithEmptyRequiredFields() {
        return new UserBuilder()
                .withFirstName("")
                .withLastName("")
                .withEmail("")
                .withCpf("")
                .build();
    }

    /**
     * Cria um User menor de idade.
     */
    public static User aUserUnderAge() {
        LocalDate birthDate = LocalDate.now().minusYears(15);
        return new UserBuilder()
                .withBirthDate(birthDate.format(DATE_FORMAT))
                .build();
    }

    // ==================== Metodos Fluentes ====================

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public UserBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder withBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Constroi o objeto User final.
     *
     * @return Instancia de User
     */
    public User build() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .cpf(cpf)
                .password(password)
                .confirmPassword(confirmPassword)
                .phone(phone)
                .birthDate(birthDate)
                .gender(gender)
                .build();
    }
}
