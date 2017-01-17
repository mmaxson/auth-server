

INSERT INTO auth.users(username,password,enabled) VALUES ('marku','$2a$04$pc0IBab7wgA81Zn/xxElXOa/wjqAXklWFP9a4G6O57QzWQVhIdfKS', true);
INSERT INTO auth.users(username,password,enabled) VALUES ('mmurun','$2a$04$ATW2Gkpurt.07828Q12WNuy90tpH22yutdrErbv2wKGGws4zVetRK', true);


INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_USER');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO auth.user_roles (USER_ID, role) VALUES (2, 'ROLE_USER');


insert into AUTH.oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES ('trusted-client', 'oauth-resource', 'trusted-client-secret','read, write, trust', 'password, authorization_code, refresh_token, implicit','http://localhost:7771','ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 60, 600, null, null);