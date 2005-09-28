-- $Id: equipmenttype.sql,v 1.13 2005/09/28 05:32:19 arseniy Exp $

CREATE TABLE EquipmentType (
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
 name VARCHAR2(128 CHAR),
 manufacturer VARCHAR2(128 CHAR),
 manufacturer_code VARCHAR2(32 CHAR),
--
 CONSTRAINT eqptype_pk PRIMARY KEY (id),
 CONSTRAINT eqptype_uniq UNIQUE (codename),
 CONSTRAINT eqptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT eqptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE equipmenttype_seq ORDER;
