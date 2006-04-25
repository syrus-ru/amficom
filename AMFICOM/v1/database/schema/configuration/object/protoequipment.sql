-- $Id: protoequipment.sql,v 1.3 2006/04/25 07:34:33 arseniy Exp $

CREATE TABLE ProtoEquipment (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
--
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 manufacturer VARCHAR2(128 CHAR),
 manufacturer_code VARCHAR2(32 CHAR),
--
 CONSTRAINT peq_pk PRIMARY KEY (id),
 CONSTRAINT peq_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT peq_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT peq_eqt_fk FOREIGN KEY (type_id)
  REFERENCES EquipmentType (id) ON DELETE CASCADE
);

CREATE SEQUENCE ProtoEquipment_seq ORDER;
