CREATE TABLE Evaluation (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
 measurement_id VARCHAR2(32),
--
 threshold_set_id VARCHAR2(32) NOT NULL,
 etalon_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT eva_pk PRIMARY KEY (id),
 CONSTRAINT eva_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT eva_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT eva_evatype_fk FOREIGN KEY (type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
 CONSTRAINT eva_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT eva_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE,
--
 CONSTRAINT eva_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES "Set" (id) ON DELETE CASCADE,
 CONSTRAINT eva_eta_fk FOREIGN KEY (ETALON_ID)
  REFERENCES "Set" (id) ON DELETE CASCADE
 );

CREATE SEQUENCE evaluaition_seq ORDER;
