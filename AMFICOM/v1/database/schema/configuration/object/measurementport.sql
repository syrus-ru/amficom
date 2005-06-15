-- $Id: measurementport.sql,v 1.8 2005/06/15 09:40:34 bass Exp $

CREATE TABLE MeasurementPort (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
-- 
 kis_id NUMBER(19),
 port_id NUMBER(19),
--
 CONSTRAINT mp_pk PRIMARY KEY (id),
 CONSTRAINT mp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE,
--
 CONSTRAINT mp_port_fk FOREIGN KEY (port_id)
  REFERENCES Port (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementport_seq ORDER;

