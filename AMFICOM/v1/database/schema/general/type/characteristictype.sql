-- $Id: characteristictype.sql,v 1.9 2005/06/15 17:03:09 bass Exp $

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
 data_type NUMBER(2) NOT NULL,
 sort NUMBER(2) NOT NULL,
--
 CONSTRAINT chctype_pk PRIMARY KEY (id),
 CONSTRAINT chctype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT chctype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT chctype_uniq UNIQUE (codename),
 CONSTRAINT chctype_dt_fk FOREIGN KEY (data_type)
  REFERENCES DataType (id) ON DELETE CASCADE
);

CREATE SEQUENCE characteristictype_seq ORDER;
