CREATE TABLE Server (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 domain_id Identifier,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 user_id Identifier NOT NULL,
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
 CONSTRAINT server_user_fk FOREIGN KEY (user_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE server_seq ORDER;
