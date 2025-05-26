--liquibase formatted sql

--changeset susanchezzz:44
CREATE TABLE employees.department_snapshot
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(256) NOT NULL UNIQUE
);