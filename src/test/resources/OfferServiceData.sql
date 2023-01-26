insert into customer (id, email, password, first_name, last_name, credit_id)
values (6,'offer_service_test2@email.com','customer','Name','Lastname',null);
insert into base_service (id, base_name)
values (6,'BaseService5');
insert into sub_service (id, base_price, description, sub_name, base_service_id)
values (6,100,'description','SubService5',6);
insert into expert (id, email, password, first_name, last_name, average_score)
values (6,'offer_service_test@email.com','expert12','Name','LastName',0);
insert into customer_order (id, description, preferred_date, price, status, customer_id, sub_service_id)
values (6,'Order description',NOW(),200,'WAITING_FOR_EXPERT_OFFER',6,6);
