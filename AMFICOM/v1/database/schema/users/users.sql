-- $Id: users.sql,v 1.1 2005/01/18 09:23:26 arseniy Exp $

CREATE TABLE users(
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 login VARCHAR2(32) NOT NULL,
 sort NUMBER(2, 0) NOT NULL,
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT users_pk PRIMARY KEY (id),
 CONSTRAINT users_creator_fk FOREIGN KEY (creator_id)
  REFERENCES users (id) ON DELETE CASCADE,
 CONSTRAINT users_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES users (id) ON DELETE CASCADE,
 CONSTRAINT users_login_uniq UNIQUE (login)
);

CREATE SEQUENCE users_seq ORDER;

