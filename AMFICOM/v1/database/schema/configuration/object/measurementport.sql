CREATE TABLE MeasurementPort (
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
-- 
 kis_id VARCHAR2(32),
 port_id VARCHAR2(32),
--
 CONSTRAINT mp_pk PRIMARY KEY (id),
 CONSTRAINT mp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_port_fk FOREIGN KEY (port_id)
  REFERENCES Port (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementport_seq ORDER;

