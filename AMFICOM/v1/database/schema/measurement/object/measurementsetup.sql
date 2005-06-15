-- $Id: measurementsetup.sql,v 1.8 2005/06/15 09:40:35 bass Exp $

CREATE TABLE MeasurementSetup (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 parameter_set_id NUMBER(19) NOT NULL,
 criteria_set_id NUMBER(19),
 threshold_set_id NUMBER(19),
 etalon_id NUMBER(19),
 description VARCHAR2(256 CHAR),
 measurement_duration NUMBER(20),
--
 CONSTRAINT mntsetup_pk PRIMARY KEY (id),
 CONSTRAINT mntsetup_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mntsetup_parset_fk FOREIGN KEY (parameter_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_etalon_fk FOREIGN KEY (etalon_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementsetup_seq ORDER;
