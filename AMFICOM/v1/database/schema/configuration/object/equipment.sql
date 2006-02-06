-- $Id: equipment.sql,v 1.18 2005/09/29 09:04:00 arseniy Exp $

CREATE TABLE Equipment (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id,
--
 proto_equipment_id NUMBER(19) NOT NULL,
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 image_id,
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
 CONSTRAINT eqp_peq_fk FOREIGN KEY (proto_equipment_id)
  REFERENCES ProtoEquipment (id) ON DELETE CASCADE,
 CONSTRAINT eqp_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE
);

CREATE SEQUENCE Equipment_seq ORDER;
