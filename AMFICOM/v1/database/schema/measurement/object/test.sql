-- $Id: test.sql,v 1.20 2005/09/25 12:29:27 arseniy Exp $

CREATE TABLE Test (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 temporal_type NUMBER(2, 0) NOT NULL,
 start_time DATE NOT NULL,
 end_time DATE NOT NULL,
 temporal_pattern_id NUMBER(19),
--
 measurement_type_code NOT NULL,
 analysis_type_code,
 group_test_id,
--
 status NUMBER(2, 0) NOT NULL,
 monitored_element_id NOT NULL,
 description VARCHAR2(256 CHAR),
 number_of_measurements NUMBER(10) NOT NULL,
--
 CONSTRAINT test_pk PRIMARY KEY (id),
 CONSTRAINT test_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT test_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
-- CONSTRAINT test_ctp_fk FOREIGN KEY (cron_temporal_pattern_id)
--  REFERENCES CronTemporalPattern (id) ON DELETE CASCADE,
--
 CONSTRAINT test_mnttype_fk FOREIGN KEY (measurement_type_code)
  REFERENCES MeasurementType (code) ON DELETE CASCADE,
 CONSTRAINT test_anatype_fk FOREIGN KEY (analysis_type_code)
  REFERENCES AnalysisType (code) ON DELETE CASCADE,
 CONSTRAINT test_grouptest_fk FOREIGN KEY (group_test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
--
 CONSTRAINT test_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

CREATE TABLE TestStopLink (
 test_id NOT NULL,
 stop_time DATE NOT NULL,
 stop_reason VARCHAR2(256 CHAR),
--
 CONSTRAINT tslink_test_uniq UNIQUE (test_id, stop_time),
 CONSTRAINT tslink_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE
);

CREATE SEQUENCE Test_seq ORDER;
