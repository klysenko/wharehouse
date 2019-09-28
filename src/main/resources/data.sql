insert into owner (login, first_name, last_name, company_name) values ('klysen', 'Kate', 'Lysenko', 'Luxoft');
insert into owner (login, first_name, last_name, company_name) values ('mlysen', 'Maksym', 'Lysenko', 'Luxoft');
insert into owner (login, first_name, last_name, company_name) values ('iivan', 'Ivan', 'Ivanov', 'A');

insert into owner_contacts (owner_id, contact, contact_type) values (1, '098-686-75-09', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (1, 'katya@gmail.com', 'private email');
insert into owner_contacts (owner_id, contact, contact_type) values (2, 'maks@gmail.com', 'private email');
insert into owner_contacts (owner_id, contact, contact_type) values (2, '098-686-75-10', 'private phone');
insert into owner_contacts (owner_id, contact, contact_type) values (3, '098-686-75-15', 'private phone');

insert into CATEGORY (title, description) values ('clothes', 'any clothes');
insert into CATEGORY (title, description) values ('devices', 'any devices');
insert into CATEGORY (title, description) values ('food', 'any food');

insert into ITEM (title, price, owner_id) values ('dress', 250.00, 1);
insert into ITEM_CATEGORY (item_id, category_id) values (1, 1);
insert into ITEM_HISTORY (item_id, date, count, status) values (1, '2019-01-14 21:32:29.24', 100, 'STORED');
insert into ITEM_HISTORY (item_id, date, count, status) values (1, '2019-09-14 21:32:29.23', 100, 'WITHDRAWED');