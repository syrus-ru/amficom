-- $Id: measurementresultparameter.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE MeasurementResultParameter (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 measurement_id NOT NULL,
 value BLOB NOT NULL,
--
 CONSTRAINT mrp_pk PRIMARY KEY (id),
 CONSTRAINT mrp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mrp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mrp_pt_fk FOREIGN KEY (type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
 CONSTRAINT mrp_m_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MeasurementResultParameter_seq ORDER;
