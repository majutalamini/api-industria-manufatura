CREATE TABLE ordens_producao (
    id                    BIGSERIAL    PRIMARY KEY,
    maquina_id            BIGINT       NOT NULL REFERENCES maquinas (id),
    operador_id           BIGINT       NOT NULL REFERENCES operadores (id),
    produto               VARCHAR(100) NOT NULL,
    quantidade_planejada  INTEGER      NOT NULL CHECK (quantidade_planejada > 0),
    quantidade_produzida  INTEGER      CHECK (quantidade_produzida >= 0),
    quantidade_defeito    INTEGER      CHECK (quantidade_defeito >= 0),
    status                VARCHAR(20)  NOT NULL DEFAULT 'EM_ANDAMENTO'
                          CHECK (status IN ('EM_ANDAMENTO', 'APROVADA', 'REPROVADA')),
    data_inicio           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fim              TIMESTAMP,
    CHECK (quantidade_defeito <= quantidade_produzida)
);

-- Insere dados de exemplo para representar diferentes cenários de produção e permitir testar as regras de 
--negócio do sistema.
--
-- 1. Ordem aprovada: 100 unidades produzidas e 2 defeituosas (2%).
--    Conforme a regra R3, o torno voltou ao status DISPONIVEL.
--
-- 2. Ordem reprovada: 80 unidades produzidas e 15 defeituosas (18,75%).
--    Conforme a regra R3, a prensa passou para EM_MANUTENCAO.
--
-- 3. Ordem em andamento: a fresadora está em EM_PRODUCAO (R1).
--    Conforme a regra R2, Ana possui apenas esta ordem em aberto.
INSERT INTO ordens_producao
    (maquina_id, operador_id, produto, quantidade_planejada, quantidade_produzida, quantidade_defeito, status, data_inicio, data_fim)
VALUES
    (1, 1, 'Eixo 20mm',      100, 100,  2,    'APROVADA',     '2026-10-06 07:00', '2026-10-06 11:00'),
    (3, 3, 'Chapa estampada', 80,  80, 15,    'REPROVADA',    '2026-10-06 22:00', '2026-10-07 04:00'),
    (2, 2, 'Engrenagem 40T',  50, NULL, NULL, 'EM_ANDAMENTO', '2026-10-08 14:00', NULL);
