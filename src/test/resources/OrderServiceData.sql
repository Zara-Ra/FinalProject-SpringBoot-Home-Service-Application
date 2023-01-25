insert into customer (id, email, password, first_name, last_name, credit_id)
values (5,'order_service_test@email.com','customer','Name','Lastname',null);
insert into base_service (id, base_name)
values (5,'BaseService5');
insert into sub_service (id, base_price, description, sub_name, base_service_id)
values (5,100,'description','SubService5',5);