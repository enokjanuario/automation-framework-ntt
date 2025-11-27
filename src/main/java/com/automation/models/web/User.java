package com.automation.models.web;

import java.util.Objects;

/**
 * Modelo que representa um Usuario para cadastro Web.
 *
 * Arquitetura:
 * - Builder pattern para criacao fluente
 * - Campos mapeados para formulario de cadastro Netshoes
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String password;
    private String confirmPassword;
    private String phone;
    private String birthDate;
    private String gender;

    public User() {
    }

    public User(String firstName, String lastName, String email, String cpf, String password,
                String confirmPassword, String phone, String birthDate, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Obtem o nome completo do usuario.
     *
     * @return Nome completo
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Obtem o CPF formatado (xxx.xxx.xxx-xx).
     *
     * @return CPF formatado
     */
    public String getFormattedCpf() {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }

    /**
     * Valida se as senhas conferem.
     *
     * @return true se senhas sao iguais
     */
    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Valida campos obrigatorios.
     *
     * @return true se todos campos obrigatorios estao preenchidos
     */
    public boolean hasRequiredFields() {
        return isNotEmpty(firstName) &&
                isNotEmpty(lastName) &&
                isNotEmpty(email) &&
                isNotEmpty(cpf) &&
                isNotEmpty(password);
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    /**
     * Builder class for User.
     */
    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String cpf;
        private String password;
        private String confirmPassword;
        private String phone;
        private String birthDate;
        private String gender;

        UserBuilder() {
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public User build() {
            return new User(firstName, lastName, email, cpf, password, confirmPassword, phone, birthDate, gender);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(cpf, user.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, cpf);
    }
}
