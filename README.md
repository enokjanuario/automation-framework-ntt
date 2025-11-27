# Automation Framework

[![CI - Automation Tests](https://github.com/enokjanuario/automation-framework-ntt/actions/workflows/ci.yml/badge.svg)](https://github.com/enokjanuario/automation-framework-ntt/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18.1-green.svg)](https://www.selenium.dev/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-brightgreen.svg)](https://rest-assured.io/)

Framework de automacao de testes profissional para APIs REST e aplicacoes Web, desenvolvido com Java, RestAssured e Selenium WebDriver.

## Indice

1. [Visao Geral](#visao-geral)
2. [Arquitetura](#arquitetura)
3. [Tecnologias](#tecnologias)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Pre-requisitos](#pre-requisitos)
6. [Instalacao](#instalacao)
7. [Execucao dos Testes](#execucao-dos-testes)
8. [Relatorios](#relatorios)
9. [Padroes e Boas Praticas](#padroes-e-boas-praticas)
10. [Decisoes Arquiteturais](#decisoes-arquiteturais)
11. [APIs Testadas](#apis-testadas)
12. [Cenarios Web](#cenarios-web)

---

## Visao Geral

Este framework foi desenvolvido seguindo principios de Clean Code, Clean Architecture, SOLID e melhores praticas de automacao de testes. Oferece uma estrutura robusta, escalavel e de facil manutencao para testes de API e Web.

### Principais Caracteristicas

- Arquitetura em camadas bem definida
- Padrao AAA (Arrange-Act-Assert) em todos os testes
- Page Object Model (POM) avancado para testes Web
- Service Layer para encapsulamento de chamadas API
- Test Data Builder para geracao de dados de teste
- Waits explicitos customizados (sem Thread.sleep)
- Validacao de JSON Schema
- Relatorios com Allure Report
- Execucao paralela com JUnit 5
- Configuracoes por ambiente (dev, staging, prod)
- Logs estruturados com Logback

---

## Arquitetura

O framework segue uma arquitetura em camadas inspirada em Clean Architecture:

```
+------------------------------------------------------------------+
|                        CAMADA DE TESTES                          |
|  (tests/api/, tests/web/)                                        |
+------------------------------------------------------------------+
                              |
+------------------------------------------------------------------+
|                      CAMADA DE SERVICOS                          |
|  (services/ - RestCountriesService, JsonPlaceholderService)      |
+------------------------------------------------------------------+
                              |
+------------------------------------------------------------------+
|                       CAMADA DE PAGES                            |
|  (pages/ - HomePage, LoginPage, RegisterPage, etc.)              |
+------------------------------------------------------------------+
                              |
+------------------------------------------------------------------+
|                        CAMADA CORE                               |
|  (core/ - DriverManager, RestAssuredConfig, BaseRequest)         |
+------------------------------------------------------------------+
                              |
+------------------------------------------------------------------+
|                    CAMADA DE SUPORTE                             |
|  (utils/, builders/, models/, config/, exceptions/)              |
+------------------------------------------------------------------+
```

### Fluxo de Dados

```
Teste -> Service/Page -> Core -> Framework (RestAssured/Selenium)
                |
          Builders (dados de teste)
                |
          Models (POJOs)
```

---

## Tecnologias

| Tecnologia | Versao | Proposito |
|------------|--------|-----------|
| Java | 17 | Linguagem principal |
| Maven | 3.8+ | Gerenciamento de dependencias |
| JUnit 5 | 5.10.2 | Framework de testes |
| RestAssured | 5.4.0 | Testes de API |
| Selenium | 4.18.1 | Testes Web |
| WebDriverManager | 5.7.0 | Gerenciamento de drivers |
| Allure | 2.25.0 | Relatorios |
| Jackson | 2.16.1 | Serializacao JSON |
| Lombok | 1.18.30 | Reducao de boilerplate |
| AssertJ | 3.25.3 | Assercoes fluentes |
| Owner | 1.0.12 | Gerenciamento de configuracoes |
| JavaFaker | 1.0.2 | Geracao de dados de teste |
| Logback | 1.4.14 | Logging |

---

## Estrutura do Projeto

```
automation-framework/
|
+-- pom.xml
+-- README.md
|
+-- src/
    +-- main/java/com/automation/
    |   +-- config/
    |   |   +-- Configuration.java          # Interface Owner para configs
    |   |   +-- ConfigurationManager.java   # Singleton de configuracoes
    |   |   +-- Environment.java            # Enum de ambientes
    |   |
    |   +-- core/
    |   |   +-- BaseRequest.java            # Classe base para requisicoes HTTP
    |   |   +-- DriverManager.java          # Gerenciador ThreadLocal de WebDriver
    |   |   +-- RestAssuredConfig.java      # Configuracao centralizada RestAssured
    |   |
    |   +-- services/
    |   |   +-- RestCountriesService.java   # Service para API RESTCountries
    |   |   +-- JsonPlaceholderService.java # Service para API JSONPlaceholder
    |   |
    |   +-- pages/
    |   |   +-- BasePage.java               # Classe base para Page Objects
    |   |   +-- HomePage.java               # Page Object - Home (Netshoes)
    |   |   +-- LoginPage.java              # Page Object - Login (Netshoes)
    |   |   +-- RegisterPage.java           # Page Object - Cadastro (Netshoes)
    |   |   +-- SearchResultsPage.java      # Page Object - Resultados (Netshoes)
    |   |   +-- ProductDetailPage.java      # Page Object - Detalhes (Netshoes)
    |   |   +-- CartPage.java               # Page Object - Carrinho (Netshoes)
    |   |
    |   +-- models/
    |   |   +-- api/
    |   |   |   +-- Post.java               # Modelo para Post (JSONPlaceholder)
    |   |   |   +-- Country.java            # Modelo para Country (RESTCountries)
    |   |   |   +-- ApiError.java           # Modelo para erros de API
    |   |   +-- web/
    |   |       +-- User.java               # Modelo para Usuario Web
    |   |       +-- Product.java            # Modelo para Produto
    |   |
    |   +-- builders/
    |   |   +-- PostBuilder.java            # Builder para Post
    |   |   +-- UserBuilder.java            # Builder para User (com CPF valido)
    |   |
    |   +-- utils/
    |   |   +-- WaitUtils.java              # Utilitarios de espera explicita
    |   |   +-- ElementUtils.java           # Utilitarios de manipulacao de elementos
    |   |   +-- ScreenshotUtils.java        # Utilitarios de screenshot
    |   |
    |   +-- exceptions/
    |       +-- WaitTimeoutException.java   # Excecao customizada para timeouts
    |       +-- PageLoadException.java      # Excecao para erros de carregamento
    |
    +-- test/
        +-- java/com/automation/tests/
        |   +-- api/
        |   |   +-- BaseApiTest.java            # Base para testes de API
        |   |   +-- RestCountriesApiTest.java   # Testes RESTCountries
        |   |   +-- JsonPlaceholderApiTest.java # Testes JSONPlaceholder
        |   |
        |   +-- web/
        |       +-- BaseWebTest.java            # Base para testes Web
        |       +-- NetshoesRegistrationTest.java # Testes de cadastro (Netshoes)
        |       +-- NetshoesCartTest.java       # Testes de carrinho (Netshoes)
        |
        +-- resources/
            +-- config/
            |   +-- default.properties          # Configuracoes padrao
            |   +-- dev.properties              # Configuracoes desenvolvimento
            |   +-- staging.properties          # Configuracoes homologacao
            |   +-- prod.properties             # Configuracoes producao
            |
            +-- schemas/
            |   +-- country-schema.json         # JSON Schema para Country
            |   +-- post-schema.json            # JSON Schema para Post
            |
            +-- allure.properties               # Configuracoes Allure
            +-- junit-platform.properties       # Configuracoes JUnit 5
            +-- logback-test.xml                # Configuracoes de log
```

---

## Pre-requisitos

- Java JDK 17 ou superior
- Maven 3.8 ou superior
- Google Chrome (para testes Web)
- Allure CLI (para visualizar relatorios)

### Instalacao do Allure CLI

Windows (via Scoop):
```bash
scoop install allure
```

MacOS (via Homebrew):
```bash
brew install allure
```

Linux:
```bash
sudo apt-add-repository ppa:qameta/allure
sudo apt-get update
sudo apt-get install allure
```

---

## Instalacao

1. Clone o repositorio:
```bash
git clone <url-do-repositorio>
cd automation-framework
```

2. Instale as dependencias:
```bash
mvn clean install -DskipTests
```

---

## Execucao dos Testes

### Executar todos os testes
```bash
mvn clean test
```

### Executar apenas testes de API
```bash
mvn clean test -Papi-tests
```

### Executar apenas testes Web
```bash
mvn clean test -Pweb-tests
```

### Executar em modo headless
```bash
mvn clean test -Pweb-tests -Pheadless
```

### Executar com ambiente especifico
```bash
mvn clean test -Denv=staging
```

### Executar teste especifico
```bash
# Testes de API
mvn clean test -Dtest=RestCountriesApiTest
mvn clean test -Dtest=JsonPlaceholderApiTest

# Testes Web - Netshoes
mvn clean test -Dtest=NetshoesRegistrationTest
mvn clean test -Dtest=NetshoesCartTest
```

### Executar com browser especifico
```bash
mvn clean test -Pweb-tests -Dweb.browser=firefox
mvn clean test -Pweb-tests -Dweb.browser=edge
```

---

## Relatorios

### Gerar relatorio Allure
```bash
mvn allure:serve
```

Ou para gerar o relatorio estatico:
```bash
mvn allure:report
```

O relatorio sera gerado em `target/site/allure-maven-plugin/`

### Visualizar relatorio gerado
```bash
allure serve target/allure-results
```

---

## Padroes e Boas Praticas

### Padrao AAA (Arrange-Act-Assert)

Todos os testes seguem o padrao AAA de forma explicita:

```java
@Test
void shouldCreateNewPost() {
    // ===================== ARRANGE =====================
    // Preparacao: Configura dados e pre-condicoes
    Post newPost = PostBuilder.aPostFromSpecification();

    // ===================== ACT =====================
    // Execucao: Realiza a acao sendo testada
    Response response = jsonPlaceholderService.createPost(newPost);

    // ===================== ASSERT =====================
    // Verificacao: Valida os resultados
    assertThat(response.getStatusCode()).isEqualTo(201);
    assertThat(response.jsonPath().getInt("id")).isPositive();
}
```

### Page Object Model (POM)

Cada pagina e representada por uma classe que encapsula seus elementos e acoes:

```java
public class RegisterPage extends BasePage {

    private static final By EMAIL_INPUT = By.cssSelector("input[type='email']");

    public RegisterPage fillEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }
}
```

### Test Data Builder

Geracao de dados de teste flexivel e reutilizavel:

```java
// Dados aleatorios validos
User user = UserBuilder.aValidUser();

// CPF invalido para teste de validacao
User user = UserBuilder.aUserWithInvalidCpf();

// Customizacao fluente
User user = new UserBuilder()
    .withEmail("custom@email.com")
    .withCpf("12345678901")
    .build();
```

### Waits Explicitos (Sem Thread.sleep)

O framework utiliza waits explicitos robustos:

```java
// Espera elemento ficar visivel
WebElement element = WaitUtils.waitForVisible(locator);

// Espera elemento ficar clicavel com timeout customizado
WebElement element = WaitUtils.waitForClickable(locator, 20);

// Espera condicao customizada
WaitUtils.waitForCondition(driver ->
    driver.findElements(locator).size() > 5
);
```

---

## Decisoes Arquiteturais

### 1. Separacao de Camadas

A arquitetura em camadas permite:
- **Baixo acoplamento**: Mudancas em uma camada nao afetam outras
- **Alta coesao**: Cada camada tem responsabilidade unica
- **Testabilidade**: Cada camada pode ser testada isoladamente
- **Manutencao**: Facil localizacao e correcao de problemas

### 2. Service Layer para APIs

Services encapsulam toda a logica de comunicacao com APIs:
- Centraliza configuracoes de endpoint
- Reutiliza RequestSpecifications
- Facilita manutencao quando APIs mudam
- Integra automaticamente com Allure

### 3. ThreadLocal para WebDriver

O DriverManager usa ThreadLocal para:
- Suportar execucao paralela de testes
- Garantir isolamento entre threads
- Evitar conflitos de estado

### 4. Owner para Configuracoes

A biblioteca Owner foi escolhida para:
- Carregar properties de multiplas fontes
- Suportar override via system properties
- Fornecer type-safety para configuracoes
- Facilitar troca de ambientes

### 5. Builder Pattern para Dados de Teste

Builders permitem:
- Criar objetos complexos de forma fluente
- Gerar dados validos com Faker
- Criar cenarios especificos (CPF invalido, etc.)
- Manter testes legíveis

### 6. Allure para Relatorios

Allure foi escolhido por:
- Relatorios visuais e interativos
- Integracao nativa com JUnit 5 e RestAssured
- Suporte a screenshots e anexos
- Historico de execucoes
- Categorizacao por Epic/Feature/Story

---

## APIs Testadas

### RESTCountries API

**Base URL:** https://restcountries.com/v3.1

**Endpoints testados:**
- `GET /name/{name}` - Busca pais por nome

**Validacoes:**
- Status code 200 para pais existente
- Status code 404 para pais inexistente
- Estrutura JSON conforme schema
- Campo `name.common` retorna valor correto
- Campos adicionais (capital, region, languages)
- Case-insensitive search
- Performance (tempo de resposta)

### JSONPlaceholder API

**Base URL:** https://jsonplaceholder.typicode.com

**Endpoints testados:**
- `GET /posts` - Lista todos os posts
- `GET /posts/{id}` - Obtem post por ID
- `POST /posts` - Cria novo post
- `PUT /posts/{id}` - Atualiza post
- `PATCH /posts/{id}` - Atualiza parcialmente
- `DELETE /posts/{id}` - Remove post

**Validacoes:**
- Status code 201 para criacao
- ID gerado na resposta
- Estrutura JSON conforme schema
- Dados persistidos corretamente
- Performance

---

## Cenarios Web

### Netshoes

**URL:** https://www.netshoes.com.br

**Cenarios implementados:**
- Cadastro de usuario com dados validos
- Validacao de CPF invalido
- Validacao de email ja existente
- Busca e adicao de produtos ao carrinho

**Nota importante sobre protecao anti-bot:**

A Netshoes implementa mecanismos de protecao anti-bot que incluem CAPTCHA, deteccao de WebDriver e rate limiting. Por esse motivo, alguns testes acessam diretamente o endpoint de login via requisicao HTTP para contornar essas protecoes durante a automacao.

**Estrategias utilizadas:**

1. **Acesso direto ao endpoint de login:** Para evitar o sistema anti-bot na interface grafica, alguns testes fazem login diretamente via API/endpoint de autenticacao, estabelecendo a sessao antes de prosseguir com os testes da interface
2. **Ambiente de teste:** Idealmente, esses testes deveriam ser executados em um ambiente de homologacao/staging com protecoes anti-bot desabilitadas
3. **Alternativas em cenarios reais:**
   - Solicitar acesso a ambiente de staging sem protecoes anti-bot
   - Whitelist de IP para testes no firewall/WAF
   - API de bypass para desabilitar CAPTCHA em ambientes de teste
   - Combinacao de testes automatizados com validacoes manuais em producao

**Arquitetura:**

Os arquivos estao organizados em:
- `src/main/java/com/automation/pages/` - Page Objects Netshoes
- `src/test/java/com/automation/tests/web/Netshoes*.java` - Testes Web

---

## Execucao em CI/CD

### GitHub Actions

```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run Tests
        run: mvn clean test -Pheadless
      - name: Generate Allure Report
        run: mvn allure:report
      - uses: actions/upload-artifact@v3
        with:
          name: allure-report
          path: target/site/allure-maven-plugin/
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Pheadless'
            }
        }
        stage('Report') {
            steps {
                allure includeProperties: false,
                       results: [[path: 'target/allure-results']]
            }
        }
    }
}
```

