create database if not exists`tp-java`
character set utf8mb4 collate utf8mb4_general_ci;
use `tp-java`;

create table if not exists users (
id int primary key auto_increment,
username varchar(50) not null unique,
password_hash varchar(255) not null,
created_at datetime not null default current_timestamp
) ENGINE = InnoDB;

create user if not exists 'tp_user'@'localhost' identified by 'TpAuth2026!';
grant select, insert, update, delete
on `tp-java`.*
to 'tp_user'@'localhost';

create table if not exists contacts (
	id int primary key auto_increment,
	user_id int,
	nom varchar(100) not null,
	prenom varchar(100) not null,
	email varchar(100),
	telephone varchar(20),
	created_at datetime not null default current_timestamp
);

alter table contacts add constraint fk_contacts_user foreign key (user_id) references users (id) on delete cascade;