CREATE TABLE Equipment (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 type_id VARCHAR2(32) NOT NULL,
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 latitude VARCHAR2(10),
 longitude VARCHAR2(10),
 hw_serial VARCHAR2(256),
 sw_serial VARCHAR2(256),
 hw_version VARCHAR2(32),
 sw_version VARCHAR2(32),
 inventory_number VARCHAR2(64),
 manufacturer VARCHAR2(64),
 manufacturer_code VARCHAR2(64),
 supplier VARCHAR2(64),
 supplier_code VARCHAR2(64),
 image_id VARCHAR2(32),
--
 mcm_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT eqp_pk PRIMARY KEY (id),
 CONSTRAINT eqp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT eqp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_epqtype_fk FOREIGN KEY (type_id)
  REFERENCES EquipmentType (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES Mcm (id) ON DELETE CASCADE
);

CREATE SEQUENCE equipment_seq ORDER;
