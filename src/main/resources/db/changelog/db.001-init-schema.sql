--liquibase formatted sql

----changeset susanchezzz:1
CREATE SCHEMA IF NOT EXISTS departments;
CREATE SCHEMA IF NOT EXISTS employees;

--changeset susanchezzz:2
CREATE TABLE IF NOT EXISTS departments.department
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(256) NOT NULL UNIQUE,
    created_at DATE         NOT NULL,
    is_main    BOOLEAN      NOT NULL
);

--changeset susanchezzz:3
CREATE TABLE IF NOT EXISTS departments.parent_child_departments
(
    id                   BIGSERIAL PRIMARY KEY,
    parent_department_id BIGINT REFERENCES departments.department (id) ON DELETE CASCADE,
    child_department_id  BIGINT REFERENCES departments.department (id) ON DELETE CASCADE,

    CONSTRAINT parent_child_unique UNIQUE (parent_department_id, child_department_id)
);

--changeset susanchezzz:4
CREATE TABLE IF NOT EXISTS departments.payment_in_department_info
(
    department_id  BIGINT PRIMARY KEY
        REFERENCES departments.department (id)
            ON DELETE CASCADE,
    common_payment INTEGER DEFAULT 0
);

--changeset susanchezzz:5
CREATE TABLE IF NOT EXISTS employees.employee
(
    id              BIGSERIAL PRIMARY KEY,
    surname         VARCHAR(128) NOT NULL,
    name            VARCHAR(128) NOT NULL,
    patronymic      VARCHAR(128),
    gender          VARCHAR(16)  NOT NULL,
    birthday        DATE         NOT NULL,
    phone_number    VARCHAR(64)  NOT NULL UNIQUE,
    department_id   BIGINT REFERENCES departments.department (id) ON DELETE CASCADE,
    employment_date DATE         NOT NULL,
    quite_date      DATE,
    position        VARCHAR(128) NOT NULL,
    payment         INTEGER      NOT NULL,
    is_leader       BOOLEAN
);

--changeset susanchezzz:6
CREATE TABLE IF NOT EXISTS departments.department_leaders
(
    id            BIGSERIAL PRIMARY KEY,
    department_id BIGINT REFERENCES departments.department (id) ON DELETE CASCADE,
    employee_id   BIGINT REFERENCES employees.employee (id) ON DELETE CASCADE
);