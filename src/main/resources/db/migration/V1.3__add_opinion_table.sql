create table opinion_entity (id  bigserial not null, author varchar(255), investment_id int8, opinion_rating float8, opinion_text varchar(255), user_id int8, primary key (id));
alter table opinion_entity add constraint FKlslrhi86yjno1nj7vn1illwfr foreign key (user_id) references user_entity;
alter table user_entity alter column rating_value type float using rating_value::float;
alter table auction_entity alter column borrower_rating type double precision using borrower_rating::double precision;