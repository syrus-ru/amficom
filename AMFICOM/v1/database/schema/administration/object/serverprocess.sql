-- $Id: serverprocess.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE ServerProcess (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 server_id NOT NULL,
 user_id NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT sproc_pk PRIMARY KEY (id),
 CONSTRAINT sproc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sproc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT sproc_uniq UNIQUE (codename),
 CONSTRAINT sproc_server_fk FOREIGN KEY (server_id)
  REFERENCES Server ON DELETE CASCADE,
 CONSTRAINT sproc_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE ServerProcess_seq ORDER;
