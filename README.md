# API RESTful de Clientes com Padrões de Projeto (Spring Boot)

## Sobre o Projeto

Esta aplicação é uma **API REST** para cadastro e gerenciamento de clientes desenvolvida em **Java com Spring Boot**, criada como entrega prática do desafio de **Design Patterns** do bootcamp da **Digital Innovation One (DIO)**.

O objetivo principal do projeto é demonstrar como os padrões de projeto clássicos (GoF) são aplicados de forma natural no ecossistema Spring para construir um backend desacoplado, limpo e fácil de manter. Na prática, a API resolve um problema comum de cadastro: o usuário informa apenas o **nome** e o **CEP**, e o sistema se encarrega de consultar automaticamente os dados completos de endereço (logradouro, bairro, cidade e estado) em um serviço externo, evitar consultas redundantes no banco e devolver respostas padronizadas tanto em cenários de sucesso quanto de erro.

---

## Como Funciona na Prática

1. **Recebimento da Requisição:** O `ClienteRestController` recebe as requisições HTTP (`GET`, `POST`, `PUT`, `DELETE`) e delega a execução para a camada de serviço.
2. **Orquestração e Cache Local de Endereços:** Ao cadastrar ou atualizar um cliente, a camada de serviço verifica primeiro se o `CEP` informado já está salvo no banco de dados local (**H2**).
   * Se **já existir**, o endereço é reaproveitado diretamente do banco, economizando chamadas externas.
   * Se **não existir**, o cliente HTTP declarativo (**OpenFeign**) consulta a API pública do **ViaCEP**, salva o novo endereço no H2 e o vincula ao cliente.
3. **Tratamento Centralizado de Falhas:** Caso um cliente inexistente seja consultado ou um CEP inválido/inexistente seja enviado, o fluxo é interceptado pelo `GlobalExceptionHandler`, devolvendo um JSON estruturado com o status HTTP adequado (`404` ou `400`).

---

## Tecnologias Utilizadas

* **Java 25**
* **Spring Boot 4.1.2**
* **Spring Data JPA** (persistência e abstração de repositórios)
* **Spring Cloud OpenFeign** (integração declarativa com a API ViaCEP)
* **H2 Database** (banco de dados relacional em memória)
* **SpringDoc OpenAPI / Swagger UI (v3.0.0)** (documentação interativa e testes dos endpoints)
* **Maven** (gerenciamento de dependências e build)

---

## Padrões de Projeto Aplicados

* **Singleton:** Gerenciado nativamente pelo contêiner de Inversão de Controle (IoC) do Spring por meio das anotações `@Service`, `@Repository`, `@RestController` e `@Autowired`, garantindo instâncias únicas e reutilizáveis dos componentes na memória.
* **Strategy:** Implementado através das interfaces `ClienteService`, `ClienteRepository` e `ViaCepService`, permitindo que a regra de negócio dependa de abstrações e possa trocar implementações sem impactar os controladores.
* **Facade:** Centralizado na classe `ClienteServiceImpl`, que abstrai toda a complexidade de verificar se um endereço já existe no banco H2, consultar a API externa do ViaCEP quando necessário, persistir o endereço e vincular ao cadastro do cliente em uma única operação simples.

---

## Tratamento Global de Exceções

Para evitar erros genéricos (`500 Internal Server Error`) e garantir uma comunicação clara com quem consome a API, o projeto implementa um interceptador centralizado na classe `GlobalExceptionHandler` utilizando **`@RestControllerAdvice`**:

* **`404 Not Found` (`ResourceNotFoundException`):** Disparado ao tentar buscar ou atualizar um cliente com um `ID` inexistente no banco de dados.
* **`400 Bad Request` (`IllegalArgumentException` / `FeignException`):** Disparado quando o CEP informado possui formato inválido ou não é encontrado na base do ViaCEP.

**Exemplo de resposta de erro padronizada (`404 Not Found`):**

```json
{
  "timestamp": "2026-11-27T07:10:00.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado com o ID: 651"
}
