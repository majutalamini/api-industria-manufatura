CREATE TABLE operadores (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    matricula  VARCHAR(20)  NOT NULL UNIQUE,
    turno      VARCHAR(10)  NOT NULL
               CHECK (turno IN ('MANHA', 'TARDE', 'NOITE'))
);

INSERT INTO operadores (nome, matricula, turno) VALUES
    ('Carlos Souza', 'OP-001', 'MANHA'),
    ('Ana Lima', 'OP-002', 'TARDE'),
    ('João Pereira', 'OP-003', 'NOITE');
