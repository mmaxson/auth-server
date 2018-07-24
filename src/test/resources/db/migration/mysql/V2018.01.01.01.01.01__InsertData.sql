


INSERT INTO auth.users(username, firstname, lastname, email, password, enabled) VALUES ('tester', 'tester', 'testerelli',
'mmurun@gmail.com', '$2a$04$pXnK6pS/jn543qhgKyox8O5KlhLohkI97BhULdvoI9nj/DFVmnXx6', true);

INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_USER');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_ADMIN');


insert into AUTH.oauth_client_details( client_id, resource_ids, client_secret, scope,
                                       authorized_grant_types, web_server_redirect_uri,
                                       authorities, access_token_validity, refresh_token_validity,
                                       additional_information, autoapprove)
VALUES ('trusted-client', 'oauth-resource', 'trusted-client-secret','read, write, trust',
        'password,authorization_code,refresh_token,implicit','http://localhost:7771',
        'ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 600, 600,
         null, null);

