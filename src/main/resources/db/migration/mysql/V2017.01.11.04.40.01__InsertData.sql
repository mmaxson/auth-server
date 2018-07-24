

INSERT INTO auth.users(username, firstname, lastname, email, password, enabled) VALUES ('marku', 'mark', 'urun',
'mmurun@gmail.com', '$2a$04$BgJqdLUnFGUSYmNs.M6qMOLIqdvxMa56A4StBQZun6UHNp6U.DN/m', true);
INSERT INTO auth.users(username, firstname, lastname, email, password, enabled) VALUES ('mmurun', 'murat', 'urun',
'mmurun@gmail.com', '$2a$04$aliVDVYwgdVUllCzdgJuxObDVoGsVc53LOH7g7gSk3BbFEJbXK29G', true);


INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_USER');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (2, 'ROLE_USER');


insert into AUTH.oauth_client_details( client_id, resource_ids, client_secret, scope,
                                       authorized_grant_types, web_server_redirect_uri,
                                       authorities, access_token_validity, refresh_token_validity,
                                       additional_information, autoapprove)
VALUES ('trusted-client', 'oauth-resource', 'trusted-client-secret','read, write, trust',
        'password,refresh_token','http://localhost:7771',
        'ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 36000, 36000,
         null, true);

insert into AUTH.oauth_client_details( client_id, resource_ids, client_secret, scope,
                                       authorized_grant_types, web_server_redirect_uri,
                                       authorities, access_token_validity, refresh_token_validity,
                                       additional_information, autoapprove)
VALUES ('fict', 'oauth-resource', 'fict-secret','read, write, trust',
        'password,authorization_code,refresh_token,implicit','http://localhost:4200',
        'ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 36000, 36000,
         null, true);