-- $Id: user.sql,v 1.3 2005/06/09 14:40:12 max Exp $

CREATE TABLE SystemUser(
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 login VARCHAR2(32) NOT NULL,
 sort NUMBER(2, 0) NOT NULL,
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT user_pk PRIMARY KEY (id),
 CONSTRAINT user_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT user_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT user_login_uniq UNIQUE (login)
);

CREATE SEQUENCE systemuser_seq ORDER;

