# API Test Automation - Desafio

Este projeto contém uma suíte completa de testes automatizados para a API REST de gerenciamento de usuários. Os testes garantem cobertura completa dos endpoints da API, incluindo cenários positivos e negativos, com integração completa em pipeline CI/CD.

## Índice

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação e Configuração](#instalação-e-configuração)
- [Execução dos Testes](#execução-dos-testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Casos de Teste](#casos-de-teste)
- [Relatórios](#relatórios)
- [Pipeline CI/CD](#pipeline-cicd)
- [Histórico de Mudanças](#histórico-de-mudanças)

## Visão Geral

### Status do Projeto
- ✅ **52 testes automatizados** executando com sucesso
- ✅ **Relatórios Allure** configurados e funcionais
- ✅ **Pipeline CI/CD** ativo no GitHub Actions
- ✅ **Deploy completo** no GitHub: [github.com/danilopuh/desafioapi](https://github.com/danilopuh/desafioapi)

### API Testada
- **Base URL:** https://serverest.dev
- **Documentação:** https://serverest.dev/#/

### Endpoints Cobertos
- `GET /usuarios` - Lista todos os usuários
- `POST /usuarios` - Cria um novo usuário  
- `GET /usuarios/{id}` - Busca usuário por ID
- `PUT /usuarios/{id}` - Atualiza usuário existente
- `DELETE /usuarios/{id}` - Remove usuário
- `POST /login` - Autenticação de usuários

### Autenticação
- Sistema de autenticação via JWT Token
- Limitação de taxa: 100 requisições por minuto

## Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| **Java** | 17 | Linguagem de programação |
| **Maven** | 3.8+ | Gerenciamento de dependências e build |
| **TestNG** | 7.8.0 | Framework de testes |
| **RestAssured** | 5.3.2 | Testes de API REST |
| **Allure** | 2.24.0 | Relatórios de teste avançados |
| **Jackson** | 2.15.2 | Serialização/deserialização JSON |
| **JavaFaker** | 1.0.2 | Geração de dados de teste |
| **Lombok** | 1.18.28 | Redução de boilerplate |
| **SLF4J + Logback** | 2.0.7 / 1.4.11 | Sistema de logs |
| **GitHub Actions** | - | Pipeline CI/CD |

## Instalação e Configuração

### Pré-requisitos

1. **Java 17** ou superior
2. **Maven 3.8** ou superior  
3. **Git** para controle de versão

### Instalação Rápida

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/desafioapi.git
cd desafioapi

# Instale as dependências
mvn clean install

# Execute os testes
mvn test

# Visualize o relatório
mvn allure:serve
```

### Windows
```cmd
# Execute o script automatizado
run-tests.bat
```

### Linux/Mac
```bash
# Dê permissão e execute
chmod +x run-tests.sh
./run-tests.sh
```

### Configuração

As configurações estão em `src/test/resources/config.properties`:

```properties
# URL base da API
api.base.url=https://serverest.dev
api.timeout=30000

# Configurações do usuário admin para testes
test.admin.email=admin.teste@exemplo.com
test.admin.password=senhaAdmin123
test.admin.name=Administrador de Testes

# Ambiente
test.environment=dev
```

## Execução dos Testes

### Comandos Maven

```bash
# Executar todos os testes
mvn test

# Executar apenas testes smoke
mvn test -Psmoke

# Executar apenas testes de regressão
mvn test -Pregression

# Executar com relatório Allure
mvn clean test allure:report

# Abrir relatório Allure no navegador
mvn allure:serve
```

### Grupos de Teste

- **smoke**: Testes críticos e essenciais
- **regression**: Todos os testes (smoke + negativos)

### Executar via TestNG XML

```bash
# Usar configuração específica do TestNG
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Executar testes específicos

```bash
# Executar uma classe específica
mvn test -Dtest=UserCrudTest

# Executar um método específico
mvn test -Dtest=UserCrudTest#testCreateUser
```

## Estrutura do Projeto

```
desafioapi/
├── .github/workflows/          # Pipeline CI/CD
│   └── api-tests.yml
├── src/
│   ├── main/java/br/com/desafioapi/
│   │   ├── config/
│   │   │   ├── ApiConfig.java       # Interface de configuração
│   │   │   └── TestConfig.java      # Configuração RestAssured
│   │   ├── models/
│   │   │   ├── User.java            # Modelo de usuário
│   │   │   ├── LoginRequest.java    # Request de login
│   │   │   ├── LoginResponse.java   # Response de login
│   │   │   ├── UserListResponse.java # Response de listagem
│   │   │   └── ApiResponse.java     # Response genérica
│   │   └── utils/
│   │       ├── AuthenticationUtils.java # Utilitários JWT
│   │       ├── TestDataGenerator.java   # Geração de dados
│   │       └── AllureUtils.java         # Utilitários Allure
│   └── test/
│       ├── java/br/com/desafioapi/tests/
│       │   ├── BaseTest.java           # Classe base
│       │   ├── UserCrudTest.java       # Testes CRUD
│       │   ├── UserNegativeTest.java   # Cenários negativos
│       │   ├── AuthenticationTest.java # Testes autenticação
│       │   └── BasicUserTest.java      # Testes básicos
│       └── resources/
│           ├── config.properties       # Configurações
│           ├── testng.xml             # Configuração TestNG
│           ├── allure.properties      # Configuração Allure
│           └── logback-test.xml       # Configuração logs
├── pom.xml                     # Configuração Maven
├── run-tests.bat              # Script Windows
└── README.md                  # Esta documentação
```

### Padrões Implementados

- **Page Object Model** (adaptado para APIs)
- **Builder Pattern** para criação de objetos de teste
- **Factory Pattern** para geração de dados
- **Singleton Pattern** para configurações
- **Fluent Interface** com RestAssured

## Casos de Teste

### Cenários Positivos

#### CRUD de Usuários
- **Listar usuários** - `GET /usuarios`
  - Verifica resposta com lista de usuários
  - Valida estrutura JSON de resposta
  - Confirma status code 200

- **Criar usuário** - `POST /usuarios`
  - Criação com dados válidos
  - Criação de usuário administrador
  - Validação de ID gerado

- **Buscar por ID** - `GET /usuarios/{id}`
  - Busca usuário existente
  - Validação de dados retornados

- **Atualizar usuário** - `PUT /usuarios/{id}`
  - Atualização com autenticação válida
  - Verificação de dados atualizados

- **Excluir usuário** - `DELETE /usuarios/{id}`
  - Exclusão com autenticação válida
  - Confirmação de remoção

#### Autenticação JWT
- **Login válido** - `POST /login`
  - Autenticação com credenciais corretas
  - Validação de token JWT retornado

- **Uso de token** - Endpoints protegidos
  - Acesso a recursos com token válido

### Cenários Negativos

#### Validação de Dados
- **Email inválido** - Formato incorreto
- **Campos obrigatórios vazios** - Nome, email, senha em branco
- **Email duplicado** - Tentativa de criar usuário com email existente

#### Erros de Busca
- **ID inexistente** - Busca por usuário que não existe
- **ID com formato inválido** - IDs malformados

#### Autenticação e Autorização
- **Login com credenciais inválidas**
  - Email inexistente
  - Senha incorreta
  - Campos vazios
  - Email com formato inválido

- **Token inválido/expirado**
  - Uso de token malformado
  - Token expirado
  - Token ausente

- **Operações sem autenticação**
  - Tentativa de atualização sem token
  - Tentativa de exclusão sem token

### Métricas de Cobertura

- **Endpoints Cobertos:** 100% (6 endpoints)
- **Cenários de Teste:** 52 casos
- **Testes Positivos:** 16 casos (smoke)
- **Testes Negativos:** 36+ casos (regression)
- **Taxa de Sucesso:** 100%

## Relatórios

### Allure Reports

Os relatórios Allure fornecem visualização rica e interativa dos resultados:

- **Dashboard** com métricas gerais
- **Detalhes** de cada teste executado
- **Attachments** com requests/responses
- **Timeline** de execução
- **Gráficos** de tendências

```bash
# Gerar e servir relatório
mvn allure:serve

# Apenas gerar (arquivos em target/site/allure-maven-plugin/)
mvn allure:report
```

### Surefire Reports

Relatórios XML/HTML padrão em `target/surefire-reports/`

### Logs Detalhados

Logs de execução salvos em `target/logs/test-execution.log`

## Pipeline CI/CD

### GitHub Actions

O pipeline é executado automaticamente em:

- **Push** para branches `main` e `develop`
- **Pull Requests** para `main`
- **Agendamento** diário às 2h AM
- **Execução manual** com opções personalizáveis

### Workflow Features

- Execução paralela de testes
- Cache de dependências Maven
- Geração automática de relatórios
- Upload de artefatos (relatórios, logs)
- Publicação do relatório Allure no GitHub Pages
- Comentários automáticos em PRs
- Execução em múltiplas versões do Java

### Artefatos Gerados

- **Relatório Allure** (HTML interativo)
- **Relatório Surefire** (XML/HTML)
- **Logs de execução** (arquivos de log)
- **Resultados TestNG** (XML)

### Como Executar

1. **Automático:** Push para branch main/develop
2. **Manual:** GitHub Actions > "API Test Automation" > "Run workflow"
3. **PR:** Automático em Pull Requests

## Histórico de Mudanças

### [1.0.0] - 2024-11-04

#### Funcionalidades Implementadas

**Testes Automatizados**
- Testes CRUD completos para todos os endpoints de usuários
- Testes de autenticação JWT com validações de token
- Cenários negativos com validação de erros e dados inválidos
- Cobertura de 100% dos endpoints da API ServerRest

**Estrutura do Projeto**
- Arquitetura robusta usando Page Object Model adaptado para APIs
- Configurações flexíveis com arquivos de propriedades
- Geração de dados dinâmica usando JavaFaker
- Logging detalhado com SLF4J e Logback
- Utilitários reutilizáveis para autenticação e dados de teste

**Pipeline CI/CD**
- GitHub Actions com execução automática
- Execução paralela de testes para otimização de tempo
- Cache de dependências Maven para performance
- Múltiplos triggers: push, PR, schedule, manual
- Artefatos de teste com retenção configurável

**Relatórios**
- Relatórios Allure interativos e detalhados  
- Relatórios Surefire padrão Maven
- Publicação automática no GitHub Pages
- Attachments com requests/responses para debug
- Métricas e dashboards de execução

**Documentação**
- README.md completo com todas as instruções
- Documentação de arquitetura e padrões
- Guias de instalação e configuração
- Casos de teste detalhados com cenários cobertos
- Scripts de execução para Windows e Linux

#### Tecnologias Utilizadas

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| **Linguagem** | Java | 17 |
| **Build Tool** | Maven | 3.8+ |
| **Test Framework** | TestNG | 7.8.0 |
| **API Testing** | RestAssured | 5.3.2 |
| **Reporting** | Allure | 2.24.0 |
| **JSON Processing** | Jackson | 2.15.2 |
| **Data Generation** | JavaFaker | 1.0.2 |
| **Logging** | SLF4J + Logback | 2.0.7 / 1.4.11 |
| **CI/CD** | GitHub Actions | - |

#### Cobertura de Testes

**Endpoints Cobertos (100%)**
- `GET /usuarios` - Listar usuários
- `POST /usuarios` - Criar usuário
- `GET /usuarios/{id}` - Buscar por ID
- `PUT /usuarios/{id}` - Atualizar usuário
- `DELETE /usuarios/{id}` - Excluir usuário
- `POST /login` - Autenticação

**Cenários de Teste (52+ casos)**
- 16 testes positivos (smoke)
- 36+ testes negativos (regression)
- Validação de dados obrigatórios e formatos
- Testes de segurança e autorização
- Tratamento de erros e códigos de status

#### Arquitetura Implementada

```
Camadas da Aplicação:
├── Configuration Layer    # Gerenciamento de configurações
├── Model Layer           # Modelos de dados e DTOs  
├── Utils Layer           # Utilitários e helpers
├── Test Layer            # Implementação dos testes
└── Reporting Layer       # Geração de relatórios
```

#### Qualidade e Boas Práticas

- Design Patterns aplicados (Builder, Factory, Singleton)
- Clean Code com nomenclatura clara e métodos pequenos
- Separation of Concerns com responsabilidades bem definidas
- DRY Principle evitando duplicação de código
- Error Handling robusto com logs detalhados
- Configuration Management externalizada
- Test Data Management com geração dinâmica

#### Métricas de Performance

- Execução paralela com 3 threads simultâneas
- Cache de dependências reduzindo tempo de build
- Relatórios otimizados com geração assíncrona
- Retry automático para testes flaky
- Timeout configurável para requisições

#### Segurança Implementada

- Autenticação JWT validada
- Gerenciamento seguro de tokens
- Validação de autorização em endpoints protegidos
- Teste de vulnerabilidades básicas
- Logs seguros sem exposição de senhas

### Próximas Melhorias

- Testes de Performance com JMeter integrado
- Testes de Contrato usando Pact
- Monitoramento Contínuo da API
- Integração com ferramentas de APM
- Testes de Acessibilidade para interfaces
- Dashboard personalizado de métricas

## Contribuindo

1. Fork o projeto
2. Crie uma feature branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Suporte

Para dúvidas ou problemas:

1. Verifique os **logs de execução**
2. Consulte a **documentação da API**: https://serverest.dev/#/
3. Abra uma **issue** no GitHub
4. Verifique os **artefatos** do pipeline

---

**Desenvolvido para garantir qualidade e confiabilidade da API**