-- $Id: analysis.sql,v 1.13 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE Analysis (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_code NOT NULL,
 monitored_element_id NOT NULL,
 measurement_id,
 name VARCHAR2(128 CHAR),
--
 criteria_set_id NOT NULL,
--
 CONSTRAINT ana_pk PRIMARY KEY (id),
 CONSTRAINT ana_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ana_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT ana_anatype_fk FOREIGN KEY (type_code)
  REFERENCES AnalysisType (code) ON DELETE CASCADE,
 CONSTRAINT ana_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT ana_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE,
--
 CONSTRAINT ana_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
);

CREATE SEQUENCE Analysis_seq ORDER;
