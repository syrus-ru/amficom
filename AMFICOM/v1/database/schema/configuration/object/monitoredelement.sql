CREATE TABLE MonitoredElement (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 name  VARCHAR2(128),
--
 measurement_port_id VARCHAR2(32) NOT NULL,
--
 sort NUMBER(2) NOT NULL,
 local_address VARCHAR2(64) NOT NULL, 
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT me_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT me_meport_fk FOREIGN KEY (measurement_port_id)
  REFERENCES MeasurementPort (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;

