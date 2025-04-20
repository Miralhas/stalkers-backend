create table refresh_token (
    created_at datetime(6),
    expires_at datetime(6) not null,
    user_id    bigint,
    id         binary(16) not null,
    primary key (id)
) engine=InnoDB;

create table role (
    id   bigint not null auto_increment,
    name enum ('ADMIN','USER') not null,
    primary key (id)
) engine=InnoDB;

create table user(
    is_oauth2_authenticated bit          not null,
    created_at              datetime(6),
    id                      bigint       not null auto_increment,
    email                   varchar(255) not null,
    password                varchar(255),
    username                varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table user_roles
(
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id)
) engine=InnoDB;

alter table user add constraint UKob8kqyqqgmefl0aco34akdtpe unique (email);
alter table user add constraint UKsb8bbouer5wak8vyiiy4pf2bx unique (username);

alter table refresh_token add constraint FKfgk1klcib7i15utalmcqo7krt foreign key (user_id) references user (id);
alter table user_roles add constraint FKrhfovtciq1l558cw6udg0h0d3 foreign key (role_id) references role (id);
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);
