CREATE TABLE Mcm (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 server_id VARCHAR2(32) NOT NULL,
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 location VARCHAR2(256),
 contact VARCHAR2(64),
 hostname VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mcm_pk PRIMARY KEY (id),
 CONSTRAINT mcm_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mcm_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_server_fk FOREIGN KEY (server_id)
  REFERENCES Server (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcm_seq ORDER;
