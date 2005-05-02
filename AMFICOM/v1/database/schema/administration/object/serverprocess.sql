CREATE TABLE ServerProcess (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 server_id VARCHAR2(32) NOT NULL,
 user_id VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT sproc_pk PRIMARY KEY (id),
 CONSTRAINT sproc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT sproc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT sproc_uniq UNIQUE (codename),
 CONSTRAINT sproc_server_fk FOREIGN KEY (server_id)
  REFERENCES Server ON DELETE CASCADE,
 CONSTRAINT sproc_user_fk FOREIGN KEY (user_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE SEQUENCE ServerProcess_seq ORDER;
