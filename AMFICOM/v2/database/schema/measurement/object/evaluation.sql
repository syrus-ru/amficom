CREATE TABLE Evaluation (
 id NUMBER(20, 0),
 type_id NUMBER(20, 0) NOT NULL,
 threshold_set_id NUMBER(20, 0) NOT NULL,
 etalon_id NUMBER(20, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT eva_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT eva_evatype_fk FOREIGN KEY (type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_eta_fk FOREIGN KEY (ETALON_ID)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE evaluaition_seq ORDER;
