CREATE TABLE MonitoredElement (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 name  VARCHAR2(128),
--
 measurement_port_id NUMBER(19) NOT NULL,
--
 sort NUMBER(2) NOT NULL,
 local_address VARCHAR2(64) NOT NULL, 
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT me_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT me_meport_fk FOREIGN KEY (measurement_port_id)
  REFERENCES MeasurementPort (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;

