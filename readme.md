# Controle de Produção Fabril

API para uma pequena fábrica registrar suas ordens de produção. O sistema impede que uma máquina ocupada receba nova ordem, limita cada operador a uma ordem por vez e manda para manutenção a máquina que produzir peças com defeito acima do limite.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC). **Etapa 1: arquitetura do backend.**

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome | GitHub |
|---|---|
| Maria Julia Talamini | [@{{majutalamini}}](https://github.com/majutalamini) |
| Lorraini Vieira | [@{{lorraini-vieira-costa}}](https://github.com/lorraini-vieira-costa) |
| Gabriel Meurer | [@{{GabrielMeurer001}}](https://github.com/GabrielMeurer001) |

## O problema

**Tema sorteado:** Indústria e Manufatura

Uma pequena fábrica controla em planilha quais máquinas estão produzindo, quem está operando e quanto foi produzido. Por isso, duas ordens acabam abertas para a mesma máquina, um operador fica com várias ordens ao mesmo tempo e máquinas que produzem muitas peças com defeito continuam rodando sem manutenção. O sistema registra as ordens de produção e aplica essas regras automaticamente, unindo controle de produção, controle de qualidade e manutenção de equipamentos.

**Quem usa o sistema:**

| Quem | O que faz no sistema |
|---|---|
| Supervisor de produção | Cadastra máquinas, abre ordens de produção e as finaliza informando quantidade produzida e com defeito |
| Operador | É vinculado à ordem que executa; nesta etapa não acessa a API diretamente |

**Regras de negócio.**

| # | Regra | Onde está no código | Rota que usa |
|---|---|---|---|
| R1 | Só é possível abrir uma ordem para uma máquina com status `DISPONIVEL`. Ao abrir, a máquina passa para `EM_PRODUCAO` | `OrdemProducaoService` | `POST /ordens-producao`, `PATCH /ordens-producao/{id}/finalizar` |
| R2 | Um operador só pode ter uma ordem `EM_ANDAMENTO` por vez | `OrdemProducaoService` | `POST /ordens-producao` |
| R3 | Ao finalizar, se os defeitos passarem de 10% do produzido, a ordem fica `REPROVADA` e a máquina vai para `EM_MANUTENCAO`; senão, a ordem fica `APROVADA` e a máquina volta para `DISPONIVEL` | `OrdemProducaoService` | `PATCH /ordens-producao/{id}/finalizar` |

**Fora do escopo:** login e perfis de acesso, controle de estoque de matéria-prima, agendamento de manutenção preventiva e relatórios de produtividade.

## Tecnologias

| O quê | Qual |
|---|---|
| Linguagem | Java 25 |
| Framework | Spring Boot 3.5 (Web, Validation, Data JPA) |
| Banco de dados | PostgreSQL 16, em Docker |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi (Swagger UI) |

## Arquitetura

A API é organizada em camadas. Uma requisição entra pelo Controller, passa pelo Service, que aplica as regras de negócio, e chega ao banco pelo Repository; a resposta faz o caminho de volta. O Mapper converte DTO em entidade e entidade em DTO.

**Mapa da estrutura.**

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|---|---|---|---|---|---|---|---|
| Maquina | `V1__criar_maquinas.sql`, {{situação}} | `Maquina`, {{situação}} | `MaquinaRepository`, {{situação}} | `MaquinaRequestDTO`, `MaquinaResponseDTO`, {{situação}} | `MaquinaMapper`, {{situação}} | `MaquinaService`, {{situação}} | `MaquinaController`, {{situação}} |
| Operador | `V2__criar_operadores.sql`, {{situação}} | `Operador`, {{situação}} | `OperadorRepository`, {{situação}} | `OperadorRequestDTO`, `OperadorResponseDTO`, {{situação}} | `OperadorMapper`, {{situação}} | `OperadorService`, {{situação}} | `OperadorController`, {{situação}} |
| OrdemProducao | `V3__criar_ordens_producao.sql`, {{situação}} | `OrdemProducao`, {{situação}} | `OrdemProducaoRepository`, {{situação}} | `OrdemProducaoRequestDTO`, `FinalizarOrdemRequestDTO`, `OrdemProducaoResponseDTO`, {{situação}} | `OrdemProducaoMapper`, {{situação}} | `OrdemProducaoService`, {{situação}} | `OrdemProducaoController`, {{situação}} |

## Entidades

| Entidade | O que representa | Como se relaciona |
|---|---|---|
| Maquina | Um equipamento da fábrica, com status `DISPONIVEL`, `EM_PRODUCAO` ou `EM_MANUTENCAO` | Aparece em várias ordens de produção |
| Operador | O funcionário que executa a produção, identificado por matrícula e turno | Aparece em várias ordens de produção, mas só uma em andamento por vez |
| OrdemProducao | Um lote a produzir em uma máquina, com quantidades planejada, produzida e com defeito | Pertence a uma máquina e a um operador |

Atributos, tipos e o diagrama das tabelas estão em [docs/modelo-de-dominio.md](docs/modelo-de-dominio.md).

## Rotas

### As que já funcionam

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `POST` | `/maquinas` | Cadastra uma máquina (começa `DISPONIVEL`) | Nenhuma | `201` | `400` |
| `GET` | `/maquinas` | Lista as máquinas | Nenhuma | `200` | — |
| `GET` | `/maquinas/{id}` | Busca uma máquina pelo id | Nenhuma | `200` | `404` |
| `POST` | `/ordens-producao` | Abre uma ordem de produção | R1 e R2 | `201` | `400`, `404` |
| `PATCH` | `/ordens-producao/{id}/finalizar` | Finaliza a ordem com as quantidades produzida e com defeito | R3 | `200` | `400`, `404` |

### Erros

Todo erro sai no mesmo formato. Este é o `400` de uma regra de negócio violada:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R1: a máquina não está disponível",
  "caminho": "/ordens-producao",
  "timestamp": "2026-10-08T19:30:00",
  "campos": []
}
```

Um campo inválido também devolve `400`, com a lista `campos` dizendo qual campo corrigir. Um id que não existe devolve `404`.

### As planejadas

Estão no contrato, mas ainda não respondem.

| Verbo | Caminho | O que vai fazer | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| — | `/operadores` | Cadastro e consulta de operadores (próximas etapas) | — | — | — |
| — | `/maquinas/{id}/liberar` | Tira a máquina da manutenção (próximas etapas) | — | — | — |

O contrato completo, com todas as rotas e os DTOs, está em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

```powershell
docker compose up -d      # sobe o PostgreSQL
mvn spring-boot:run       # inicia a API (Flyway cria as tabelas)
mvn test                  # testes das regras R1, R2 e R3
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Situação do projeto

| Parte | Situação |
|---|---|
| Desenho | Problema e regras definidos neste README |
| Estrutura | Camadas, entidades e migrations V1 a V3 prontas |
| API e regras | Rotas de `/maquinas` e `/ordens-producao` com R1, R2 e R3, mais testes unitários das regras |

**Ainda não funciona:** rotas de `/operadores` e `/maquinas/{id}/liberar`.

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |

| [Como usar o Docker](docs/como-usar-docker.md) | Instalação necessária, inicialização do PostgreSQL, execução da API e solução de problemas comuns |
