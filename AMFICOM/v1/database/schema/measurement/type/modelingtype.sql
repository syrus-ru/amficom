-- $Id: modelingtype.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

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
 CONSTRAINT modtype_pk PRIMARY KEY (id),
 CONSTRAINT modtype_uniq UNIQUE (codename),
 CONSTRAINT modtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT modtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE ModelingType_seq ORDER;
