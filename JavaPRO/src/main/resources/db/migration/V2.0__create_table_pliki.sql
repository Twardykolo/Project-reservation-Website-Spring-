DROP TABLE IF EXISTS projekty.pliki CASCADE;

CREATE TABLE projekty.pliki
(
    id   VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    type VARCHAR(255),
    data OID,
    CONSTRAINT pk_pliki PRIMARY KEY (id)
);