insert into owner (login, first_name, last_name, company_name) values ('klysen', 'Kate', 'Lysenko', 'Luxoft');
insert into owner (login, first_name, last_name, company_name) values ('mlysen', 'Maksym', 'Lysenko', 'Luxoft');
insert into owner (login, first_name, last_name, company_name) values ('iivan', 'Ivan', 'Ivanov', 'A');
insert into owner (login, first_name, last_name, company_name) values ('nlysen', 'Nika', 'Lysenko', 'B');
insert into owner (login, first_name, last_name, company_name) values ('jdoe', 'John', 'Doe', 'C');

insert into owner_contacts (owner_id, contact, contact_type) values (1, '098-686-75-09', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (1, 'katya@gmail.com', 'private email');
insert into owner_contacts (owner_id, contact, contact_type) values (2, 'maks@gmail.com', 'private email');
insert into owner_contacts (owner_id, contact, contact_type) values (2, '098-686-75-10', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (3, '098-686-75-11', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (4, '098-686-75-12', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (5, '098-686-75-13', 'private phone');

insert into CATEGORY (title, description) values ('clothes', 'any clothes');
insert into CATEGORY (title, description) values ('devices', 'any devices');
insert into CATEGORY (title, description) values ('food', 'any food');

insert into ITEM (title, price, owner_id) values ('dress', 100.00, 1);
insert into ITEM_CATEGORY (item_id, category_id) values (1, 1);
insert into ITEM_HISTORY (item_id, date, count, status) values (1, '2019-01-14 21:32:29.24', 500, 'STORED');
insert into ITEM_HISTORY (item_id, date, count, status) values (1, '2019-09-14 21:32:29.23', 100, 'WITHDRAWED');

insert into ITEM (title, price, owner_id) values ('pants', 100.00, 2);
insert into ITEM_CATEGORY (item_id, category_id) values (2, 1);
insert into ITEM_HISTORY (item_id, date, count, status) values (2, '2019-06-14 21:32:29.24', 1000, 'STORED');

insert into ITEM (title, price, owner_id) values ('water', 100.00, 3);
insert into ITEM_CATEGORY (item_id, category_id) values (3, 3);
insert into ITEM_HISTORY (item_id, date, count, status) values (3, '2019-07-14 21:32:29.24', 5000, 'STORED');
insert into ITEM_HISTORY (item_id, date, count, status) values (3, '2019-09-14 21:32:29.23', 1000, 'WITHDRAWED');

insert into ITEM (title, price, owner_id) values ('phones', 100.00, 4);
insert into ITEM_CATEGORY (item_id, category_id) values (4, 2);
insert into ITEM_HISTORY (item_id, date, count, status) values (4, '2019-03-14 21:32:29.24', 70, 'STORED');
insert into ITEM_HISTORY (item_id, date, count, status) values (1, '2019-09-14 21:32:29.23', 200, 'WITHDRAWED');

insert into ITEM (title, price, owner_id) values ('hats',100.00, 5);
insert into ITEM_CATEGORY (item_id, category_id) values (5, 1);
insert into ITEM_HISTORY (item_id, date, count, status) values (5, '2018-12-14 21:32:29.24', 110, 'STORED');
