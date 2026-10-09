CREATE TABLE operadores (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    turno      VARCHAR(10)  NOT NULL
               CHECK (turno IN ('MANHA', 'TARDE', 'NOITE'))
);

INSERT INTO operadores (nome, turno) VALUES
    ('Carlos Souza', 'MANHA'),
    ('Ana Lima', 'TARDE'),
    ('João Pereira', 'NOITE');
