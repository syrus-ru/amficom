CREATE TABLE Server (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 user_id VARCHAR2(32) NOT NULL,
 location VARCHAR2(256),
 contact VARCHAR2(64),
 hostname VARCHAR2(32) NOT NULL,
 sessions NUMBER(10, 0),
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
