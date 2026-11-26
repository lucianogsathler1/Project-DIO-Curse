# API RESTful de Clientes com Padrões de Projeto (Spring Boot)

Projeto desenvolvido como desafio prático do bootcamp da **Digital Innovation One (DIO)**, explorando a implementação dos principais **Design Patterns** aplicados ao ecossistema **Spring Framework**. A aplicação consiste em uma API REST para gerenciamento de clientes, realizando a consulta e o preenchimento automático de endereços a partir do CEP por meio de integração com a API pública do **ViaCEP**.

---

## Tecnologias Utilizadas

* **Java 25**
* **Spring Boot 4.1.2**
* **Spring Data JPA** (persistência de dados)
* **Spring Cloud OpenFeign** (consumo declarativo da API externa ViaCEP)
* **H2 Database** (banco de dados relacional em memória)
* **SpringDoc OpenAPI / Swagger UI (v3.0.0)** (documentação interativa e testes dos endpoints)
* **Maven** (gerenciamento de dependências e build)

---

## Padrões de Projeto Aplicados

A arquitetura do projeto aplica três padrões clássicos de desenvolvimento na prática:

* **Singleton:** Gerenciado nativamente pelo contêiner de Inversão de Controle (IoC) do Spring por meio das anotações `@Service`, `@Repository`, `@RestController` e `@Autowired`, garantindo instâncias únicas e reutilizáveis dos componentes na memória.
* **Strategy:** Implementado através das interfaces `ClienteService`, `ClienteRepository` e `ViaCepService`, permitindo que a regra de negócio dependa de abstrações e possa trocar implementações sem impactar os controladores.
* **Facade:** Centralizado na classe `ClienteServiceImpl`, que abstrai toda a complexidade de verificar se um endereço já existe no banco H2, consultar a API externa do ViaCEP quando necessário, persistir o endereço e vincular ao cadastro do cliente em uma única operação simples.

---

## Endpoints da API

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/clientes` | Lista todos os clientes cadastrados |
| `GET` | `/clientes/{id}` | Busca um cliente específico pelo ID |
| `POST` | `/clientes` | Cadastra um novo cliente e preenche o endereço via CEP |
| `PUT` | `/clientes/{id}` | Atualiza os dados e o endereço de um cliente existente |
| `DELETE` | `/clientes/{id}` | Remove um cliente do banco de dados |

---


## Como Executar Localmente

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/lucianogsathler1/Project-DIO-Curse] / (https://github.com/lucianogsathler1/Project-DIO-Curse)
