CREATE TABLE MeasurementSetup (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 parameter_set_id Identifier NOT NULL,
 criteria_set_id Identifier,
 threshold_set_id Identifier,
 etalon_id Identifier,
 description VARCHAR2(256),
 measurement_duration NUMBER(20),
--
 CONSTRAINT mntsetup_pk PRIMARY KEY (id),
 CONSTRAINT mntsetup_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT mntsetup_parset_fk FOREIGN KEY (parameter_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_criset_fk FOREIGN KEY (criteria_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_thrset_fk FOREIGN KEY (threshold_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE,
 CONSTRAINT mntsetup_etalon_fk FOREIGN KEY (etalon_id)
  REFERENCES Sett (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementsetup_seq ORDER;
