CREATE TABLE Test (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 temporal_type NUMBER(2, 0) NOT NULL,
 start_time DATE,
 end_time DATE,
 pt_template_id NUMBER(20, 0),
 tt_timestamps TimeStampArray,
 measurement_type_id NUMBER(20, 0) NOT NULL,
 analysis_type_id NUMBER(20, 0),
 evaluation_type_id NUMBER(20, 0),
 status NUMBER(2, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,
 return_type NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),

 CONSTRAINT test_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT test_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,

 CONSTRAINT test_ttt_fk FOREIGN KEY (temporal_type)
  REFERENCES TestTemporalType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_pttt_fk FOREIGN KEY (pt_template_id)
  REFERENCES PTTemporalTemplate (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE ENABLE,
CONSTRAINT test_anatype_fk FOREIGN KEY (analysis_type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE ENABLE,
CONSTRAINT test_evatype_fk FOREIGN KEY (evaluation_type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_teststatus_fk FOREIGN KEY (status)
  REFERENCES TestStatus (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT test_trt_fk FOREIGN KEY (return_type)
  REFERENCES TestReturnType (id) ON DELETE CASCADE ENABLE
 )
 NESTED TABLE tt_timestamps STORE AS tt_timestamps_tab;

CREATE SEQUENCE test_seq ORDER;
