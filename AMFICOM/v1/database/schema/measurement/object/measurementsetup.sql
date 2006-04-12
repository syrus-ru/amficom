-- $Id: measurementsetup.sql,v 1.9.2.4 2006/04/12 13:50:39 arseniy Exp $

CREATE TABLE MeasurementSetup (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 measurement_port_type_id NOT NULL,
 measurement_template_id NOT NULL,
 analysis_template_id,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT ms_pk PRIMARY KEY (id),
 CONSTRAINT ms_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ms_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT ms_mpt_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE,
 CONSTRAINT ms_mtlp_fk FOREIGN KEY (measurement_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE,
 CONSTRAINT ms_atlp_fk FOREIGN KEY (analysis_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);

CREATE SEQUENCE MeasurementSetup_seq ORDER;
