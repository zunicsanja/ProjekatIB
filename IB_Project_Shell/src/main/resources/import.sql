INSERT INTO USERS (id, username, email, password, last_password_reset_date, certificate, enabled) VALUES (1, 'user', 'user@example.com', '$2a$10$7YYsG/KPPkCrQb04Tg3bUOrZ7WZAznMHLuq0HlPxtAMzsUVUYhdRiMzsUVUYhdRi', '2017-10-01 21:58:58.508-07', null , true);
INSERT INTO USERS (id, username, email, password, last_password_reset_date, certificate, enabled) VALUES (2, 'admin', 'admin@example.com', '$2a$10$ar5q31zaso4V3dRAJSchfu8iL6unAtAiiWhuNOQiBALssHqED3jbK', '2017-10-01 18:57:58.508-07', null, true);

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 2);
