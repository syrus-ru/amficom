-- $Id: users.sql,v 1.6 2004/10/13 15:59:11 bass Exp $

CREATE TABLE mcm.users(
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 login VARCHAR2(32) NOT NULL,
 sort NUMBER(2, 0) NOT NULL,
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT users_pk PRIMARY KEY (id),
 CONSTRAINT users_creator_fk FOREIGN KEY (creator_id)
  REFERENCES mcm.users (id) ON DELETE CASCADE,
 CONSTRAINT users_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES mcm.users (id) ON DELETE CASCADE,
 CONSTRAINT users_login_fk FOREIGN KEY (login)
  REFERENCES amficom.users (login) ON DELETE CASCADE,
 CONSTRAINT users_name_fk FOREIGN KEY (name)
  REFERENCES amficom.users (name) ON DELETE CASCADE
);

CREATE SEQUENCE users_seq ORDER;

