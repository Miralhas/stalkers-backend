# set foreign_key_checks = 0;

# delete from refresh_token;
# delete from password_reset_token;
# delete from role;
# delete from user_roles;
# delete from user;
# delete from chapter;
# delete from novel_tags;
# delete from novel;

# set foreign_key_checks = 1;

# alter table refresh_token auto_increment = 1;
# alter table password_reset_token auto_increment = 1;
# alter table role auto_increment = 1;
# alter table user auto_increment = 1;
# alter table chapter auto_increment = 1;
# alter table novel_tags auto_increment = 1;
# alter table novel auto_increment = 1;

# insert into role(name) values('ADMIN'), ('USER');
#
# insert into user(email, username, created_at, password, is_oauth2_authenticated) VALUES ('admin@admin.com', 'admin', utc_timestamp, '$2a$10$CNr3GCdNp8wLUGP/XbUqHOjkSA2josmmAHu38jQgP11g/P/Xulgoa', false);
# insert into user_roles(role_id, user_id) VALUES (1,1);
# insert into user_roles(role_id, user_id) VALUES (2,1);
#
# insert into user(email, username, created_at, password, is_oauth2_authenticated) VALUES ('abc@gmail.com', 'abc', utc_timestamp, '$2a$10$CNr3GCdNp8wLUGP/XbUqHOjkSA2josmmAHu38jQgP11g/P/Xulgoa', false);
# insert into user_roles(role_id, user_id) VALUES (2,2);