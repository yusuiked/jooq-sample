create table author (
    id      int primary key
    ,name   varchar(512)
);

create table book (
    isbn            varchar(64) primary key
    ,title          varchar(512)
    ,author_id      int
    ,publish_date   date
    ,foreign key (author_id) references author (id)
);

insert into author (id,name)　values (1, 'Arthur Conan Doyle');
insert into author (id,name) values (2, 'Haruki Murakami');
insert into author (id,name) values (3, '江戸川乱歩');
insert into author (id,name) values (4, '諫山創');

insert into book (isbn,title,author_id,publish_date) values ('001-0000000001', 'A Study in Scarlet', 1, '1887-01-01');
insert into book (isbn,title,author_id,publish_date) values ('001-0000000002', 'The Sign of Four', 1, '1890-01-01');
insert into book (isbn,title,author_id,publish_date) values ('001-8888880001', '少年探偵団', 3, '1937-01-01');
insert into book (isbn,title,author_id,publish_date) values ('001-9999990001', 'Norwegian Wood', 2, '1987-01-01');
insert into book (isbn,title,author_id,publish_date) values ('001-9999990002', '1Q84, Volumes 1-2', 2, '2009-01-01');
