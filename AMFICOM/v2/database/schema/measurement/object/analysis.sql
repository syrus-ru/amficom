CREATE TABLE Analysis (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 type_id NUMBER(20, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,

 criteria_set_id NUMBER(20, 0) NOT NULL,
 
 CONSTRAINT ana_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT ana_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT ana_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,

 CONSTRAINT ana_anatype_fk FOREIGN KEY (type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT ana_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE

 CONSTRAINT ana_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
);

CREATE SEQUENCE analysis_seq ORDER;
