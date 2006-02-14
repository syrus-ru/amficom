-- $Id: characteristictype.sql,v 1.10.2.1 2006/02/14 09:44:22 arseniy Exp $

CREATE TABLE CharacteristicType (
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
 name VARCHAR2(128 CHAR) NOT NULL,
 data_type_code NUMBER(2, 0) NOT NULL,
 sort NUMBER(2) NOT NULL,
--
 CONSTRAINT chctype_pk PRIMARY KEY (id),
 CONSTRAINT chctype_uniq UNIQUE (codename),
 CONSTRAINT chctype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT chctype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT chctype_dt_fk FOREIGN KEY (data_type_code)
  REFERENCES DataType (code) ON DELETE CASCADE
);

CREATE SEQUENCE CharacteristicType_seq ORDER;
