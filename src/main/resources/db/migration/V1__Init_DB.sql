create table task
(
    id              integer not null auto_increment,
    completion_date date,
    done            bit,
    finish_date     date,
    id_worker       integer,
    name            varchar(255),
    start_date      date,
    primary key (id)
) engine = InnoDB;

create table worker
(
    id       integer not null auto_increment,
    initials varchar(255),
    surname  varchar(255),
    primary key (id)
) engine = InnoDB;
