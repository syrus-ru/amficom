-- $Id: monitoredelement.sql,v 1.1.2.1 2006/02/14 10:01:07 arseniy Exp $

CREATE TABLE MonitoredElement (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NOT NULL,
--
 name VARCHAR2(128 CHAR),
--
 measurement_port_id NOT NULL,
--
 kind NUMBER(2) NOT NULL,
 local_address VARCHAR2(64 CHAR) NOT NULL, 
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT me_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT me_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT me_mp_fk FOREIGN KEY (measurement_port_id)
  REFERENCES MeasurementPort (id) ON DELETE CASCADE
);

CREATE SEQUENCE MonitoredElement_seq ORDER;

