--liquibase formatted sql

--changeset susanchezzz:1
INSERT INTO security.account
(id, employee_id, username, email, password, role)
VALUES (1, 1, 'admin', 'admin@example.com', '$2a$10$SRqFe6NECuj4aLdeXtcAOuOztg6bcJxxMKIm/RLE20k8.fpIOP1O6', 'ROLE_ADMIN'),
       (2, 2, 'user', 'user@example.com', '$2a$10$Rp69ZPOCFIMAWT2GncqzPe3hmZRI1VKAsSl2lu6fGq60St0SBhncC', 'ROLE_USER');

SELECT SETVAL('security.account_id_seq', 2);