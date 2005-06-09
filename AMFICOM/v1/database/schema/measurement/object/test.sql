CREATE TABLE Test (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 temporal_type NUMBER(2, 0) NOT NULL,
 start_time DATE,
 end_time DATE,
 cron_temporal_pattern_id NUMBER(19),
--
 measurement_type_id NUMBER(19) NOT NULL,
 analysis_type_id NUMBER(19),
 evaluation_type_id NUMBER(19),
 group_test_id	VARCHAR(32),
--
 status NUMBER(2, 0) NOT NULL,
 monitored_element_id NUMBER(19) NOT NULL,
 return_type NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),
 number_of_measurements NUMBER(10) NOT NULL,
--
 CONSTRAINT test_pk PRIMARY KEY (id),
 CONSTRAINT test_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT test_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT test_ctp_fk FOREIGN KEY (cron_temporal_pattern_id)
  REFERENCES CronTemporalPattern (id) ON DELETE CASCADE,
--
 CONSTRAINT test_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
CONSTRAINT test_anatype_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
CONSTRAINT test_evatype_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE,
CONSTRAINT test_grouptest_fk FOREIGN KEY (group_test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
--
 CONSTRAINT test_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE test_seq ORDER;
