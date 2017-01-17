

drop schema if exists auth;
create schema auth;

RUNSCRIPT FROM 'classpath:db/migration/mysql/V2017.01.11.04.30.01__InitializeDB.sql';

INSERT INTO auth.users(username,password,enabled) VALUES ('tester','$2a$04$9T6a/79wcPZ6M9/RV7Dh7.kVH8ygNd0Nb8nxJUreauF3FmYeOufyO', true);


INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_USER');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_ADMIN');


insert into AUTH.oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES ('trusted-client', 'oauth-resource', 'trusted-client-secret', 'read, write, trust', 'password, authorization_code, refresh_token, implicit','http://localhost:7771','ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 60, 600, null, null);

SET SCHEMA AUTH;