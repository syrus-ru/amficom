CREATE TABLE Equipment (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 domain_id Identifier,
--
 monitored_element_id Identifier,
--
 type_id Identifier NOT NULL,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 image_id Identifier,
--
 sort NUMBER(2) NOT NULL,
 kis_id Identifier,
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
 CONSTRAINT eqp_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_epqtype_fk FOREIGN KEY (type_id)
  REFERENCES EquipmentType (id) ON DELETE CASCADE,
--
 CONSTRAINT eqp_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE
);

CREATE SEQUENCE equipment_seq ORDER;

