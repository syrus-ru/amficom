CREATE TABLE Test (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 temporal_type NUMBER(2, 0) NOT NULL,
 start_time DATE,
 end_time DATE,
 temporal_pattern_id VARCHAR2(32),
--
 measurement_type_id VARCHAR2(32) NOT NULL,
 analysis_type_id VARCHAR2(32),
 evaluation_type_id VARCHAR2(32),
--
 status NUMBER(2, 0) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
 return_type NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT test_pk PRIMARY KEY (id),
 CONSTRAINT test_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT test_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT test_tp_fk FOREIGN KEY (temporal_pattern_id)
  REFERENCES TemporalPattern (id) ON DELETE CASCADE,
--
 CONSTRAINT test_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
CONSTRAINT test_anatype_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
CONSTRAINT test_evatype_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
--
 CONSTRAINT test_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE test_seq ORDER;
