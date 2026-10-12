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

-- Dados de exemplo coerentes com as regras:
-- 1. Aprovada: 2 defeitos em 100 (2%), Torno voltou a DISPONIVEL (R3)
-- 2. Reprovada: 15 defeitos em 80 (18,75%), Prensa foi para EM_MANUTENCAO (R3)
-- 3. Em andamento: Fresadora está EM_PRODUCAO (R1); Ana tem só esta aberta (R2)
INSERT INTO ordens_producao
    (maquina_id, operador_id, produto, quantidade_planejada, quantidade_produzida, quantidade_defeito, status, data_inicio, data_fim)
VALUES
    (1, 1, 'Eixo 20mm',      100, 100,  2,    'APROVADA',     '2026-10-06 07:00', '2026-10-06 11:00'),
    (3, 3, 'Chapa estampada', 80,  80, 15,    'REPROVADA',    '2026-10-06 22:00', '2026-10-07 04:00'),
    (2, 2, 'Engrenagem 40T',  50, NULL, NULL, 'EM_ANDAMENTO', '2026-10-08 14:00', NULL);
