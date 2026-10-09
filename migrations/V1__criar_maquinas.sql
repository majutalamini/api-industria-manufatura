CREATE TABLE maquinas (
    id      BIGSERIAL    PRIMARY KEY,
    codigo  VARCHAR(20)  NOT NULL UNIQUE,
    nome    VARCHAR(100) NOT NULL,
    setor   VARCHAR(50)  NOT NULL,
    status  VARCHAR(20)  NOT NULL DEFAULT 'DISPONIVEL'
            CHECK (status IN ('DISPONIVEL', 'EM_PRODUCAO', 'EM_MANUTENCAO'))
);

INSERT INTO maquinas (codigo, nome, setor, status) VALUES
    ('TOR-01', 'Torno CNC',         'Usinagem',  'DISPONIVEL'),
    ('FRE-01', 'Fresadora',         'Usinagem',  'EM_PRODUCAO'),
    ('PRE-01', 'Prensa Hidráulica', 'Estamparia', 'EM_MANUTENCAO');
