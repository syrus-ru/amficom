CREATE TABLE Analysis (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
 measurement_id VARCHAR2(32),
--
 criteria_set_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT ana_pk PRIMARY KEY (id),
 CONSTRAINT ana_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT ana_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT ana_anatype_fk FOREIGN KEY (type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT ana_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT ana_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCEDE,
--
 CONSTRAINT ana_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES "Set" (id) ON DELETE CASCADE
);

CREATE SEQUENCE analysis_seq ORDER;
