CREATE TABLE Characteristic (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 value VARCHAR2(256),
--
 sort NUMBER(2) NOT NULL,
 domain_id VARCHAR2(32),
 server_id VARCHAR2(32),
 mcm_id VARCHAR2(32),
 equipment_id VARCHAR2(32),
 transmission_path_id VARCHAR2(32),
 port_id VARCHAR2(32),
 kis_id VARCHAR2(32),
--
 CONSTRAINT chc_pk PRIMARY KEY (id),
 CONSTRAINT chc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT chc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT chc_chctype_fk FOREIGN KEY (type_id)
  REFERENCES CharacteristicType (id) ON DELETE CASCADE,
--
 CONSTRAINT chc_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
 CONSTRAINT chc_server_fk FOREIGN KEY (server_id)
  REFERENCES Server (id) ON DELETE CASCADE,
 CONSTRAINT chc_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES MCM (id) ON DELETE CASCADE,
 CONSTRAINT chc_eqp_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT chc_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT chc_port_fk FOREIGN KEY (port_id)
  REFERENCES Port (id) ON DELETE CASCADE,
 CONSTRAINT chc_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE,
 
);

CREATE SEQUENCE characteristic_seq ORDER;

