CREATE TABLE Result (
 id NUMBER(20, 0),
 measurement_id NUMBER(20, 0) NOT NULL,
 analysis_id NUMBER(20, 0),
 evaluation_id NUMBER(20, 0),
 sort NUMBER(2, 0) NOT NULL,
 alarm_level NUMBER(2, 0) NOT NULL,
 CONSTRAINT res_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT res_mnt_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT res_ana_fk FOREIGN KEY (analysis_id)
  REFERENCES Analysis (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT res_eva_fk FOREIGN KEY (evaluation_id)
  REFERENCES Evaluation (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT res_sort_fk FOREIGN KEY (sort)
  REFERENCES ResultSort (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT res_al_fk FOREIGN KEY (alarm_level)
  REFERENCES AlarmLevel (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE result_seq ORDER;
