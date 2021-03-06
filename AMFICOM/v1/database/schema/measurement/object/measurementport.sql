-- $Id: measurementport.sql,v 1.2 2005/10/03 10:08:51 arseniy Exp $

CREATE TABLE MeasurementPort (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
-- 
 kis_id NOT NULL,
 port_id,
--
 CONSTRAINT mp_pk PRIMARY KEY (id),
 CONSTRAINT mp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_type_fk FOREIGN KEY (type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_port_fk FOREIGN KEY (port_id)
  REFERENCES Port (id) ON DELETE SET NULL
);

CREATE SEQUENCE measurementport_seq ORDER;

