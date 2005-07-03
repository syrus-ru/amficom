-- $Id: evaluation.sql,v 1.11 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Evaluation (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 monitored_element_id NOT NULL,
 measurement_id,
--
 threshold_set_id NOT NULL,
 etalon_id NOT NULL,
--
 CONSTRAINT eva_pk PRIMARY KEY (id),
 CONSTRAINT eva_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT eva_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT eva_evatype_fk FOREIGN KEY (type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
 CONSTRAINT eva_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT eva_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE,
--
 CONSTRAINT eva_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT eva_eta_fk FOREIGN KEY (etalon_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE
 );

CREATE SEQUENCE evaluaition_seq ORDER;
