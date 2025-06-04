--liquibase formatted sql

--changeset susanchezzz:22
CREATE TABLE IF NOT EXISTS employees.employee
(
    id              BIGSERIAL PRIMARY KEY,
    surname         VARCHAR(128) NOT NULL,
    name            VARCHAR(128) NOT NULL,
    patronymic      VARCHAR(128),
    gender          VARCHAR(16)  NOT NULL,
    birthday        DATE         NOT NULL,
    phone_number    VARCHAR(64)  NOT NULL UNIQUE,
    department_id   BIGINT       NOT NULL,
    employment_date DATE         NOT NULL,
    quite_date      DATE,
    position        VARCHAR(128) NOT NULL,
    payment         INTEGER      NOT NULL,
    is_leader       BOOLEAN
);