CREATE TABLE Server (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 type_id VARCHAR2(32),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 hostname VARCHAR2(64) NOT NULL,
 user_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT server_pk PRIMARY KEY (id),
 CONSTRAINT server_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT server_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT server_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT server_servertype_fk FOREIGN KEY (type_id)
  REFERENCES ServerType (id) ON DELETE CASCADE,
--
 CONSTRAINT server_user_fk FOREIGN KEY (user_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE server_seq ORDER;

