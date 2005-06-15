-- $Id: equipment.sql,v 1.15 2005/06/15 09:40:34 bass Exp $

CREATE TABLE Equipment (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 type_id NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 image_id NUMBER(19),
 supplier VARCHAR2(128 CHAR),
 supplier_code VARCHAR2(32 CHAR),
 latitude NUMBER,
 longitude NUMBER,
 hw_serial VARCHAR2(64 CHAR),
 hw_version VARCHAR2(64 CHAR),
 sw_serial VARCHAR2(64 CHAR),
 sw_version VARCHAR2(64 CHAR),
 inventory_number VARCHAR2(64 CHAR),
--
 CONSTRAINT eqp_pk PRIMARY KEY (id),
 CONSTRAINT eqp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT eqp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_epqtype_fk FOREIGN KEY (type_id)
  REFERENCES EquipmentType (id) ON DELETE CASCADE,
 CONSTRAINT eqp_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE
);

CREATE SEQUENCE equipment_seq ORDER;
