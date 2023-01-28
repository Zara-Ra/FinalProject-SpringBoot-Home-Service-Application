insert into customer (id, email, password, first_name, last_name, credit_id)
values (5,'order_service_test@email.com','customer','Name','Lastname',null);

insert into base_service (id, base_name)
values (5,'BaseService5');

insert into sub_service (id, base_price, description, sub_name, base_service_id)
values (5,100,'description','SubService5',5);

insert into expert (id, email, password, first_name, last_name, average_score)
values (5,'order_service_test_test@email.com','expert12','Name','LastName',0);

insert into expert_offer (id, duration, preferred_date, price, expert_id)
values (5,20000,NOW(),200,5);

insert into expert_offer (id, duration, preferred_date, price, expert_id)
values (55,10000,NOW(),300,5);

insert into customer_order (id, description, preferred_date, price, status, customer_id, sub_service_id)
values (5,'nothing',NOW(),100,'WAITING_FOR_EXPERT_OFFER',5,5);

insert into customer_order_expert_offer_list (customer_order_id, expert_offer_list_id)
values (5,5);

insert into customer_order_expert_offer_list (customer_order_id, expert_offer_list_id)
values (5,55);