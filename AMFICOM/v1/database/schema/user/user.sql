-- $Id: user.sql,v 1.1 2005/01/31 14:22:40 arseniy Exp $

CREATE TABLE "User"(
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
 CONSTRAINT user_pk PRIMARY KEY (id),
 CONSTRAINT user_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT user_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT user_login_uniq UNIQUE (login)
);

CREATE SEQUENCE user_seq ORDER;

