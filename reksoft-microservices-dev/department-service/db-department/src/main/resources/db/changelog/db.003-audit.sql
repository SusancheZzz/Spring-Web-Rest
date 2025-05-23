--liquibase formatted sql

--changeset susanchezzz:1
CREATE OR REPLACE FUNCTION insert_into_department_payment()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO departments.payment_in_department_info(department_id)
    SELECT d.id
    FROM departments.department d
    WHERE d.id = NEW.id;

    RETURN NULL;
END
$$ language plpgsql;

--changeset susanchezzz:2
CREATE OR REPLACE TRIGGER insert_into_department_payment_after_insertion_into_department
    AFTER INSERT
    ON departments.department
    FOR EACH ROW
EXECUTE PROCEDURE insert_into_department_payment();

--changeset susanchezzz:3
CREATE SCHEMA IF NOT EXISTS audit;

--changeset susanchezzz:4
CREATE TABLE IF NOT EXISTS audit.audit_department
(
    id            BIGSERIAL PRIMARY KEY,
    department_id BIGINT      NOT NULL,
    timestamp     TIMESTAMP   NOT NULL,
    operation     VARCHAR(16) NOT NULL
);