CREATE TABLE Analysis (
 id NUMBER(20, 0),
 type_id NUMBER(20, 0) NOT NULL,
 criteria_set_id NUMBER(20, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT ana_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT ana_anatype_fk FOREIGN KEY (type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT ana_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT ana_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE analysis_seq ORDER;
