-- $Id: role.sql,v 1.1 2005/10/10 15:30:56 bob Exp $

CREATE TABLE Role(
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT role_pk PRIMARY KEY (id),
 CONSTRAINT role_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT role_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT role_codename_uniq UNIQUE (codename)
);

CREATE SEQUENCE Role_Seq ORDER;
