CREATE TABLE Evaluation (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 type_id NUMBER(20, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,

 threshold_set_id NUMBER(20, 0) NOT NULL,
 etalon_id NUMBER(20, 0) NOT NULL,

 CONSTRAINT eva_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT aeva_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_evatype_fk FOREIGN KEY (type_id)
  REFERENCES EvaluationType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE

 CONSTRAINT eva_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT eva_eta_fk FOREIGN KEY (ETALON_ID)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 );

CREATE SEQUENCE evaluaition_seq ORDER;
