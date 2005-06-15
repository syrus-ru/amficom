-- $Id: parametertype.sql,v 1.6 2005/06/15 09:40:34 bass Exp $

CREATE TABLE ParameterType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(128 CHAR) NOT NULL,
 data_type NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT partype_pk PRIMARY KEY (id),
--
 CONSTRAINT partype_uniq UNIQUE (codename),
 CONSTRAINT partype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT partype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE parametertype_seq ORDER;
