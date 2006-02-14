-- $Id: equipmenttype.sql,v 1.15.2.1 2006/02/14 09:48:41 arseniy Exp $

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
 CONSTRAINT eqt_pk PRIMARY KEY (id),
 CONSTRAINT eqt_uniq UNIQUE (codename),
 CONSTRAINT eqt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT eqt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE EquipmentType_seq ORDER;
