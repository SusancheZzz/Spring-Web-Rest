--liquibase formatted sql

--changeset susanchezzz:1
CREATE SCHEMA IF NOT EXISTS security;

--changeset susanchezzz:2
CREATE TABLE IF NOT EXISTS security.account
(
    id          BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL UNIQUE REFERENCES employees.employee (id) ON DELETE CASCADE,
    username    VARCHAR(64)  NOT NULL UNIQUE CHECK ( length(trim(username)) > 3 ),
    email       VARCHAR(128) NOT NULL UNIQUE CHECK ( length(trim(email)) > 3 ) ,
    password    VARCHAR(64)  NOT NULL CHECK ( length(trim(password)) > 3 ),
    role VARCHAR(64) NOT NULL CHECK ( length(trim(role)) > 0 )
);