-- $Id: test.sql,v 1.20.2.2 2006/02/15 19:45:52 arseniy Exp $

CREATE TABLE Test (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 description VARCHAR2(256 CHAR),
 group_test_id,
 monitored_element_id NOT NULL,
 status NUMBER(2) NOT NULL,
 temporal_type NUMBER(2) NOT NULL,
 start_time DATE NOT NULL,
 end_time DATE NOT NULL,
 temporal_pattern_id,
 measurement_type_id NOT NULL,
 number_of_measurements NUMBER(10) NOT NULL,
 analysis_type_id,
--
 CONSTRAINT t_pk PRIMARY KEY (id),
 CONSTRAINT t_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT t_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT t_t_fk FOREIGN KEY (group_test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT t_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT t_ptp_fk FOREIGN KEY (temporal_pattern_id)
  REFERENCES PeriodicalTemporalPattern (id) ON DELETE CASCADE,
 CONSTRAINT t_mt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT t_at_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE
);

CREATE SEQUENCE Test_seq ORDER;
