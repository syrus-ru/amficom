-- $Id: equipment.sql,v 1.14 2005/06/15 07:50:18 bass Exp $

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
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 image_id NUMBER(19),
 supplier VARCHAR2(128),
 supplier_code VARCHAR2(32),
 latitude NUMBER,
 longitude NUMBER,
 hw_serial VARCHAR2(64),
 hw_version VARCHAR2(64),
 sw_serial VARCHAR2(64),
 sw_version VARCHAR2(64),
 inventory_number VARCHAR2(64),
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
