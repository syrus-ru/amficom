CREATE TABLE Modeling (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR(256),
--
 monitored_element_id VARCHAR2(32),
 measurement_type_id VARCHAR2(32) NOT NULL,
 argument_set_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT modeling_pk PRIMARY KEY (id),
 CONSTRAINT modeling_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT modeling_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT modeling_tp_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT modeling_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
CONSTRAINT modeling_argset_fk FOREIGN KEY (argument_set_id)
  REFERENCES Sett (id) ON DELETE CASCADE
);

CREATE SEQUENCE modeling_seq ORDER;
