CREATE TABLE Server (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 location VARCHAR2(256),
 contact VARCHAR2(64),
 hostname VARCHAR2(32) NOT NULL,
 sessions NUMBER(10, 0),
--
 CONSTRAINT server_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT server_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT server_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE 
);

CREATE SEQUENCE server_seq ORDER;
