-- $Id: characteristic.sql,v 1.14 2006/01/16 09:10:16 bass Exp $

CREATE TABLE Characteristic (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
 value VARCHAR2(256 CHAR),
 editable NUMBER(1) NOT NULL,
 visible NUMBER(1) NOT NULL,
--
 characterizable_id NUMBER(19) NOT NULL,
--
 CONSTRAINT chc_pk PRIMARY KEY (id),
 CONSTRAINT chc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT chc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT chc_chctype_fk FOREIGN KEY (type_id)
  REFERENCES CharacteristicType (id) ON DELETE CASCADE,
 CONSTRAINT chc_uniq UNIQUE (type_id, characterizable_id)
-- 
);

CREATE SEQUENCE characteristic_seq ORDER;