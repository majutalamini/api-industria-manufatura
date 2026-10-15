# Contrato da API — Indústria e Manufatura

Documentação com todas as rotas da API, formatos de requisição, respostas de sucesso e códigos de erro.

Base URL padrão: `http://localhost:8080`

---

## 1. Máquinas (`/maquinas`)

### 1.1. Cadastrar máquina
* **Método:** `POST`
* **Rota:** `/maquinas`
* **Descrição:** Cadastra uma nova máquina no sistema. A máquina sempre inicia com o status `DISPONIVEL`.
* **Corpo da requisição (`MaquinaRequestDTO`):**
```json
{
  "codigo": "TOR-01",
  "nome": "Torno CNC",
  "setor": "Usinagem"
}
```
* **Resposta de sucesso:** `201 Created`
```json
{
  "id": 1,
  "codigo": "TOR-01",
  "nome": "Torno CNC",
  "setor": "Usinagem",
  "status": "DISPONIVEL"
}
```
* **Erros possíveis:**
  * `400 Bad Request`: Campos obrigatórios não preenchidos, tamanho excedido ou código de máquina já existente.

---

### 1.2. Listar máquinas
* **Método:** `GET`
* **Rota:** `/maquinas`
* **Descrição:** Retorna a lista com todas as máquinas cadastradas.
* **Resposta de sucesso:** `200 OK`
```json
[
  {
    "id": 1,
    "codigo": "TOR-01",
    "nome": "Torno CNC",
    "setor": "Usinagem",
    "status": "DISPONIVEL"
  }
]
```

---

### 1.3. Buscar máquina por ID
* **Método:** `GET`
* **Rota:** `/maquinas/{id}`
* **Descrição:** Retorna os dados de uma máquina específica.
* **Resposta de sucesso:** `200 OK`
```json
{
  "id": 1,
  "codigo": "TOR-01",
  "nome": "Torno CNC",
  "setor": "Usinagem",
  "status": "DISPONIVEL"
}
```
* **Erros possíveis:**
  * `404 Not Found`: Máquina não encontrada.

---

### 1.4. Liberar máquina da manutenção
* **Método:** `PATCH`
* **Rota:** `/maquinas/{id}/liberar`
* **Descrição:** Retira uma máquina do status `EM_MANUTENCAO`, retornando-a para `DISPONIVEL`.
* **Resposta de sucesso:** `200 OK`
```json
{
  "id": 1,
  "codigo": "TOR-01",
  "nome": "Torno CNC",
  "setor": "Usinagem",
  "status": "DISPONIVEL"
}
```
* **Erros possíveis:**
  * `400 Bad Request`: A máquina não está em manutenção.
  * `404 Not Found`: Máquina não encontrada.

---

## 2. Operadores (`/operadores`)

### 2.1. Cadastrar operador
* **Método:** `POST`
* **Rota:** `/operadores`
* **Descrição:** Cadastra um novo operador no sistema.
* **Valores aceitos para turno:** `MANHA`, `TARDE`, `NOITE`.
* **Corpo da requisição (`OperadorRequestDTO`):**
```json
{
  "nome": "Carlos Souza",
  "matricula": "OP-001",
  "turno": "MANHA"
}
```
* **Resposta de sucesso:** `201 Created`
```json
{
  "id": 1,
  "nome": "Carlos Souza",
  "matricula": "OP-001",
  "turno": "MANHA"
}
```
* **Erros possíveis:**
  * `400 Bad Request`: Campos obrigatórios inválidos ou matrícula duplicada.

---

### 2.2. Listar operadores
* **Método:** `GET`
* **Rota:** `/operadores`
* **Descrição:** Retorna todos os operadores cadastrados.
* **Resposta de sucesso:** `200 OK`
```json
[
  {
    "id": 1,
    "nome": "Carlos Souza",
    "matricula": "OP-001",
    "turno": "MANHA"
  }
]
```

---

### 2.3. Buscar operador por ID
* **Método:** `GET`
* **Rota:** `/operadores/{id}`
* **Descrição:** Retorna os dados de um operador pelo ID.
* **Resposta de sucesso:** `200 OK`
```json
{
  "id": 1,
  "nome": "Carlos Souza",
  "matricula": "OP-001",
  "turno": "MANHA"
}
```
* **Erros possíveis:**
  * `404 Not Found`: Operador não encontrado.

---

## 3. Ordens de Produção (`/ordens-producao`)

### 3.1. Abrir ordem de produção
* **Método:** `POST`
* **Rota:** `/ordens-producao`
* **Descrição:** Abre uma nova ordem de produção vinculando máquina e operador.
* **Regras de negócio aplicadas:**
  * **R1:** A máquina selecionada deve estar com status `DISPONIVEL`. Ao abrir a ordem, a máquina passa para `EM_PRODUCAO`.
  * **R2:** O operador selecionado não pode ter outra ordem com status `EM_ANDAMENTO`.
* **Corpo da requisição (`OrdemProducaoRequestDTO`):**
```json
{
  "maquinaId": 1,
  "operadorId": 1,
  "produto": "Eixo de Transmissão",
  "quantidadePlanejada": 100
}
```
* **Resposta de sucesso:** `201 Created`
```json
{
  "id": 1,
  "maquinaId": 1,
  "operadorId": 1,
  "produto": "Eixo de Transmissão",
  "quantidadePlanejada": 100,
  "quantidadeProduzida": null,
  "quantidadeDefeito": null,
  "status": "EM_ANDAMENTO",
  "dataAbertura": "2026-10-14T20:00:00",
  "dataFinalizacao": null
}
```
* **Erros possíveis:**
  * `400 Bad Request`: Violação de R1 (máquina indisponível), violação de R2 (operador ocupado) ou campos obrigatórios inválidos.
  * `404 Not Found`: Máquina ou operador não encontrados.

---

### 3.2. Finalizar ordem de produção
* **Método:** `PATCH`
* **Rota:** `/ordens-producao/{id}/finalizar`
* **Descrição:** Finaliza uma ordem de produção em andamento, informando quantidades produzidas e com defeito.
* **Regras de negócio aplicadas:**
  * **R3:** Se a quantidade com defeito for maior que 10% da quantidade produzida, a ordem fica `REPROVADA` e a máquina é colocada em `EM_MANUTENCAO`. Caso contrário, a ordem fica `APROVADA` e a máquina volta para `DISPONIVEL`.
  * Quantidade com defeito não pode ser maior que a quantidade produzida.
  * Uma ordem já finalizada não pode ser finalizada novamente.
* **Corpo da requisição (`FinalizarOrdemRequestDTO`):**
```json
{
  "quantidadeProduzida": 100,
  "quantidadeDefeito": 5
}
```
* **Resposta de sucesso (Aprovada - defeito <= 10%):** `200 OK`
```json
{
  "id": 1,
  "maquinaId": 1,
  "operadorId": 1,
  "produto": "Eixo de Transmissão",
  "quantidadePlanejada": 100,
  "quantidadeProduzida": 100,
  "quantidadeDefeito": 5,
  "status": "APROVADA",
  "dataAbertura": "2026-10-14T20:00:00",
  "dataFinalizacao": "2026-10-14T22:30:00"
}
```
* **Resposta de sucesso (Reprovada - defeito > 10%):** `200 OK`
```json
{
  "id": 1,
  "maquinaId": 1,
  "operadorId": 1,
  "produto": "Eixo de Transmissão",
  "quantidadePlanejada": 100,
  "quantidadeProduzida": 100,
  "quantidadeDefeito": 15,
  "status": "REPROVADA",
  "dataAbertura": "2026-10-14T20:00:00",
  "dataFinalizacao": "2026-10-14T22:30:00"
}
```
* **Erros possíveis:**
  * `400 Bad Request`: Ordem já finalizada, defeito superior à produção total ou dados inválidos.
  * `404 Not Found`: Ordem de produção não encontrada.

---

## 4. Padrão das Respostas de Erro

Todas as mensagens de erro da API seguem a estrutura padronizada da classe `ErroResponse`:

### 4.1. Regra de negócio violada (`400 Bad Request`)
```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R1: a máquina não está disponível",
  "caminho": "/ordens-producao",
  "timestamp": "2026-10-14T20:00:00",
  "campos": []
}
```

### 4.2. Erro de validação de campos (`400 Bad Request`)
```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos",
  "caminho": "/maquinas",
  "timestamp": "2026-10-14T20:00:00",
  "campos": [
    {
      "campo": "codigo",
      "mensagem": "é obrigatório"
    }
  ]
}
```

### 4.3. Recurso não encontrado (`404 Not Found`)
```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Máquina não encontrada: 99",
  "caminho": "/maquinas/99",
  "timestamp": "2026-10-14T20:00:00",
  "campos": []
}
```
