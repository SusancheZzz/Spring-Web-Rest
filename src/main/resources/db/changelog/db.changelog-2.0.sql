--liquibase formatted sql

--changeset susanchezzz:1
INSERT INTO departments.department (id, name, created_at, is_main)
VALUES (1, 'Main department', '1999-03-12', true),
       (2, 'Sales Department', '1999-03-12', false),
       (3, 'Accounting', '1999-03-12', false),
       (4, 'Human Resources Department', '1999-05-03', false),
       (5, 'IT department', '2001-06-05', false),
       (6, 'Child department for IT', '2012-09-10', false),
       (7, 'Child department for Accounting', '2003-06-16', false);

SELECT SETVAL('departments.department_id_seq', 7);

--changeset susanchezzz:2
INSERT INTO departments.parent_child_departments
    (id, parent_department_id, child_department_id)
VALUES (1, 1, 2),
       (2, 1, 3),
       (3, 1, 4),
       (4, 1, 5),
       (5, 2, 3),
       (6, 5, 4),
       (7, 2, 6),
       (8, 3, 7);

SELECT SETVAL('departments.parent_child_departments_id_seq', 8);

--changeset susanchezzz:3
INSERT INTO departments.payment_in_department_info(department_id)
SELECT d.id
FROM departments.department d;

--changeset susanchezzz:4
INSERT INTO employees.employee
(id, surname, name, patronymic, gender, birthday, phone_number, department_id, employment_date,
 position, payment, is_leader)
VALUES (1, 'Borisov', 'Boris', 'Borisovich', 'MALE', '1973-03-14', '+7 (930) 364-01-64', 1,
        '2009-08-13', 'Boss', 300000, true),
       (2, 'Igorev', 'Igor', 'Igorevich', 'MALE', '1976-10-27', '+7 (950) 856-18-58', 1,
        '2011-07-19', 'Boss deputy', 230000, false),
       (3, 'Ivanov', 'Ivan', 'Ivanovich', 'MALE', '1980-02-05', '+7 (930) 765-51-51', 2,
        '2012-09-10', 'Head of Department', 200000, true),
       (4, 'Petrov', 'Petr', 'Petrovich', 'MALE', '1991-05-25', '+7 (950) 123-34-45', 2,
        '2010-10-01', 'sales manager', 45000, false),
       (5, 'Semyonov', 'Semyon', 'Semyonovich', 'MALE', '1987-12-05', '+7 (930) 793-54-31', 2,
        '2013-08-17', 'account Manager', 50000, false),
       (6, 'Antonova', 'Anna', 'Andreevna', 'FEMALE', '1977-06-27', '+7 (950)-885-18-83', 3,
        '2007-04-12', 'Chief accountant', 170000, true),
       (7, 'Sergeev', 'Sergey', 'Sergeevich', 'MALE', '1990-05-28', '+7 (930) 876-18-01', 3,
        '2015-11-05', 'accountant', 40000, false),
       (8, 'Dmitriev', 'Dmitriy', 'Dmitrievich', 'MALE', '1989-03-19', '+7 (920) 738-17-06', 3,
        '2007-12-05', 'Senior accountant', 70000, false),
       (9, 'Viktoieva', 'Viktoria', 'Viktotovna', 'FEMALE', '1975-07-22', '+7 (950) 124-87-11', 4,
        '2002-07-03', 'Head of Department', 200000, true),
       (10, 'Ivanova', 'Elizaveta', 'Sergeevna', 'FEMALE', '1985-11-04', '+7 (930) 835-54-95', 4,
        '2010-03-13', 'Recruiter', 65000, false),
       (11, 'Michailov', 'Michail', 'Michailovich', 'MALE', '1984-08-26', '+7 (920) 847-81-47', 4,
        '2011-06-23', 'HR Specialist', 85000, false),
       (12, 'Artemov', 'Artem', 'Artemovich', 'MALE', '1973-09-04', '+7 (930) 148-97-04', 5,
        '2001-06-10', 'Team Lead', 250000, true),
       (13, 'Vitaliev', 'Vitaliy', 'Vitalievich', 'MALE', '1983-10-07', '+7 (950) 975-82-01', 5,
        '2005-02-23', 'Senior Dev', 170000, false),
       (14, 'Maximov', 'Maxim', 'Maximovich', 'MALE', '1990-11-28', '+7 (920) 473-17-94', 5,
        '2011-07-18', 'Middle Dev', 120000, false);

SELECT SETVAL('employees.employee_id_seq', 14);

--changeset susanchezzz:5
INSERT INTO departments.department_leaders
    (id, department_id, employee_id)
VALUES (1, 1, 1),
       (2, 2, 3),
       (3, 3, 6),
       (4, 4, 9),
       (5, 5, 12);

SELECT SETVAL('departments.department_leaders_id_seq', 5);

--changeset susanchezzz:6
INSERT INTO employees.account
    (id, employee_id, username, email, password)
VALUES (1, 1, 'admin', 'admin@gmail.com', 'admin'),
       (2, 2, 'igorev', 'igorev@gmail.com', 'igorev'),
       (3, 3, 'ivanov', 'ivanov@gmail.com', 'ivanov'),
       (4, 4, 'petrov', 'petrov@gmail.com', 'petrov'),
       (5, 5, 'semyonov', 'semyonov@gmail.com', 'semyonov');

--changeset susanchezzz:7
INSERT INTO employees.authority
    (id, u_authority)
VALUES (1, 'ADMIN'),
       (2, 'USER');

SELECT SETVAL('employees.authority_id_seq', 2);

--changeset susanchezzz:8
INSERT INTO employees.account_authority
    (id, employee_id, authority_id)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 2),
       (4, 4, 2),
       (5, 5, 2);

SELECT SETVAL('employees.account_authority_id_seq', 5);