-- $Id: equipmenttype.sql,v 1.10 2005/06/15 07:50:18 bass Exp $

CREATE TABLE EquipmentType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 name VARCHAR2(128),
 manufacturer VARCHAR2(128),
 manufacturer_code VARCHAR2(32),
--
 CONSTRAINT eqptype_pk PRIMARY KEY (id),
 CONSTRAINT eqptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT eqptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE equipmenttype_seq ORDER;
