


CREATE TABLE AUTH.Users (
  USER_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
  USERNAME VARCHAR(45) NOT NULL,
  PASSWORD VARCHAR(60) NOT NULL,
  ENABLED TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`USER_ID`)
  );


CREATE TABLE AUTH.User_Roles (
  USER_ROLE_ID int(11) NOT NULL AUTO_INCREMENT,
  USER_ID INT UNSIGNED NOT NULL,
  ROLE varchar(45) NOT NULL,
  PRIMARY KEY (USER_ROLE_ID),
  UNIQUE KEY UNIQ_USER_ID_ROLE (ROLE, USER_ID),
  KEY USER_ID_IX (USER_ID),
  CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID) REFERENCES users (USER_ID)
  );

create table AUTH.oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

INSERT INTO users(username,password,enabled) VALUES ('marku','$2a$04$pc0IBab7wgA81Zn/xxElXOa/wjqAXklWFP9a4G6O57QzWQVhIdfKS', true);
INSERT INTO users(username,password,enabled) VALUES ('mmurun','$2a$04$ATW2Gkpurt.07828Q12WNuy90tpH22yutdrErbv2wKGGws4zVetRK', true);


INSERT INTO user_roles (USER_ID, role) VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (USER_ID, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_roles (USER_ID, role) VALUES (2, 'ROLE_USER');


insert into AUTH.oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES ('trusted-client', 'oauth-resource', 'trusted-client-secret','read, write, trust', 'password, authorization_code, refresh_token, implicit','http://localhost:7771','ROLE_CLIENT, ROLE_TRUSTED_CLIENT', 60, 600, null, null);