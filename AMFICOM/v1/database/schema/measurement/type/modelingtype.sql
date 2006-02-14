-- $Id: modelingtype.sql,v 1.8.2.1 2006/02/14 09:53:54 arseniy Exp $

CREATE TABLE ModelingType (
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
 CONSTRAINT modt_pk PRIMARY KEY (id),
 CONSTRAINT modt_uniq UNIQUE (codename),
 CONSTRAINT modt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT modt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE ModelingType_seq ORDER;
