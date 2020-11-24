create table auction_entity (id  bigserial not null, auction_duration bytea, auction_start_date timestamp, borrower_name varchar(255), borrower_rating int4, loan_amount numeric(19, 2), loan_duration bytea, loan_rate float8, loan_risk int4, status varchar(255), primary key (id));
create table investment_entity (investment_id  bigserial not null, borrower_name varchar(255), duration bytea, lender_name varchar(255), loan_amount numeric(19, 2), loan_id int8, rate numeric(19, 2), return_amount numeric(19, 2), risk int4, status varchar(255), schedule_id int8, primary key (investment_id));
create table loan_entity (id  bigserial not null, amount numeric(19, 2), average_rate float8, borrower_name varchar(255), duration bytea, repayment numeric(19, 2), start_date timestamp, status varchar(255), schedule_id int8, primary key (id));
create table offer_entity (id  bigserial not null, amount float8, auction_id int8, borrower_name varchar(255), duration bytea, lender_name varchar(255), rate float8, risk int4, status varchar(255), primary key (id));
create table repayment_entity (id  bigserial not null, date timestamp, status varchar(255), value numeric(19, 2), primary key (id));
create table repayment_schedule_entity (id  bigserial not null, owner_id int8, type varchar(255), investment_entity_investment_id int8, loan_entity_id int8, primary key (id));
create table repayment_schedule_entity_repayments (repayment_schedule_entity_id int8 not null, repayments_id int8 not null);
create table user_entity (id  bigserial not null, email varchar(255), name varchar(255), phone_number varchar(255), platform_account_number uuid, private_account_number uuid, rating_value int4 not null, role varchar(255), surname varchar(255), user_name varchar(255), primary key (id));
alter table repayment_schedule_entity_repayments add constraint UK_lct2gu2g14dth73jgcj614sji unique (repayments_id);
alter table investment_entity add constraint FKpe0f3pfubl1aadxjedpenkfto foreign key (schedule_id) references repayment_schedule_entity;
alter table investment_entity add constraint FKbcatnhx6iuvw7okg2gwh7rmcg foreign key (loan_id) references loan_entity;
alter table loan_entity add constraint FK54ap430fyffca8hhvqna6javv foreign key (schedule_id) references repayment_schedule_entity;
alter table offer_entity add constraint FK5n7l7fduiyb6icb13n004v1mk foreign key (auction_id) references auction_entity;
alter table repayment_schedule_entity add constraint FKh1u2wxfs76g6veo1ty0kfrav4 foreign key (investment_entity_investment_id) references investment_entity;
alter table repayment_schedule_entity add constraint FKne9gde54wh76fvo0kiqr5ojp8 foreign key (loan_entity_id) references loan_entity;
alter table repayment_schedule_entity_repayments add constraint FKj4cfkjmtd88p2wvc8y4fj0n57 foreign key (repayments_id) references repayment_entity;
alter table repayment_schedule_entity_repayments add constraint FKgrghn5815eqis8w3d9j2rnhgu foreign key (repayment_schedule_entity_id) references repayment_schedule_entity;

INSERT INTO user_entity(id, email, name, phone_number, platform_account_number, private_account_number, rating_value, role, surname, user_name)
VALUES (1,'borrower@mail','Bilbo','123-123-123','d474cb1d-35b6-4d32-b290-0ab36317cdfc','77e431e8-f303-4560-9108-3cd44a001505',3,'BORROWER','Baggins','Bilbo_Baggins');
INSERT INTO user_entity(id, email, name, phone_number, platform_account_number, private_account_number, rating_value, role, surname, user_name)
VALUES (2,'Frodo@mail','Frodo','111-222-333','60ea082e-3707-401f-86e2-88a07892b416','91973752-9c9f-4080-91ae-e49d1903e9ee',3,'BORROWER','Baggins','Frodo_Baggins');
INSERT INTO user_entity( id, email, name, phone_number, platform_account_number, private_account_number, rating_value, role, surname, user_name)
VALUES (3,'Gamgee@mail','Samwise','123-456-789','e0c30b15-02e1-423f-9fa3-2a9cf411980d','6b659158-0e46-460b-b418-13bda2920361',0,'LENDER','Gamgee','Samwise_Gamgee');
