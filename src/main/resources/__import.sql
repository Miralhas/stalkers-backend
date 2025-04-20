insert into role(name) values('ADMIN'), ('USER');

INSERT INTO user(email, username, created_at, password, is_oauth2_authenticated) VALUES ('admin@admin.com', 'General Seo', utc_timestamp, '$2a$10$CNr3GCdNp8wLUGP/XbUqHOjkSA2josmmAHu38jQgP11g/P/Xulgoa', false);
insert into user_roles(role_id, user_id) VALUES (1,1);
insert into user_roles(role_id, user_id) VALUES (2,1);