--changeset anekoss:8
-- comment: insert test data into categories table
insert into categories (name) values ('Рестораны');
insert into categories (name) values ('Еда');
insert into categories (name) values ('Супермаркеты');
insert into categories (name) values ('Фастфуд');
insert into categories (name) values ('Развлечения');



--changeset anekoss:9
-- comment: insert test data into mccs table
insert into mccs (mcc) values ('5811');
insert into mccs (mcc) values ('5812');
insert into mccs (mcc) values ('5813');
insert into mccs (mcc) values ('5814');
insert into mccs (mcc) values ('5297');
insert into mccs (mcc) values ('5298');
insert into mccs (mcc) values ('4299');


--changeset anekoss:10
-- comment: insert test data into transactions table
insert into transactions (value, month) values (100, 'january');
insert into transactions (value, month) values (200, 'january');
insert into transactions (value, month) values (300, 'january');
insert into transactions (value, month) values (400, 'january');
insert into transactions (value, month) values (500, 'january');
insert into transactions (value, month) values (500, 'january');
insert into transactions (value, month) values (500, 'january');
insert into transactions (value, month) values (200.5, 'february');
insert into transactions (value, month) values (300.1, 'february');
insert into transactions (value, month) values (400.6, 'february');
insert into transactions (value, month) values (500.89, 'february');
insert into transactions (value, month) values (150, 'march');
insert into transactions (value, month) values (350, 'march');
insert into transactions (value, month) values (450, 'march');

--changeset anekoss:11
-- comment: insert test data into category_transactions table
insert into category_transactions (category_id, transaction_id) values (1, 1);
insert into category_transactions (category_id, transaction_id) values (2, 2);
insert into category_transactions (category_id, transaction_id) values (3, 3);
insert into category_transactions (category_id, transaction_id) values (3, 3);
insert into category_transactions (category_id, transaction_id) values (3, 3);

--changeset anekoss:12
-- comment: insert test data into subcategories table
insert into subcategories (category_id, subcategory_id) values (2, 1);
insert into subcategories (category_id, subcategory_id) values (2, 3);
insert into subcategories (category_id, subcategory_id) values (2, 4);
insert into subcategories (category_id, subcategory_id) values (5, 1);


--changeset anekoss:13
-- comment: insert test data into category_mccs table
insert into category_mccs (category_id, mcc_id) values (1, 1);
insert into category_mccs (category_id, mcc_id) values (1, 2);
insert into category_mccs (category_id, mcc_id) values (1, 3);
insert into category_mccs (category_id, mcc_id) values (4, 4);
insert into category_mccs (category_id, mcc_id) values (3, 5);
insert into category_mccs (category_id, mcc_id) values (2, 6);
insert into category_mccs (category_id, mcc_id) values (5, 7);