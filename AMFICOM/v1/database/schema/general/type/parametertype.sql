-- $Id: parametertype.sql,v 1.8 2005/07/17 04:53:00 arseniy Exp $

CREATE TABLE ParameterType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(128 CHAR) NOT NULL,
 data_type_code NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT partype_pk PRIMARY KEY (id),
 CONSTRAINT partype_uniq UNIQUE (codename),
 CONSTRAINT partype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT partype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT partype_dt_fk FOREIGN KEY (data_type_code)
  REFERENCES DataType (code) ON DELETE CASCADE
);

CREATE SEQUENCE parametertype_seq ORDER;
