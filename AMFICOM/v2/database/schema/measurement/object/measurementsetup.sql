CREATE TABLE MeasurementSetup (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
--
 parameter_set_id NUMBER(20, 0) NOT NULL,
 criteria_set_id NUMBER(20, 0),
 threshold_set_id NUMBER(20, 0),
 etalon_id NUMBER(20, 0),
 description VARCHAR2(256),
 measurement_duration NUMBER(20),
--
 CONSTRAINT mntsetup_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT mntsetup_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mntsetup_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
--
 CONSTRAINT mntsetup_parset_fk FOREIGN KEY (parameter_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mntsetup_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mntsetup_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mntsetup_etalon_fk FOREIGN KEY (etalon_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE measurementsetup_seq ORDER;
