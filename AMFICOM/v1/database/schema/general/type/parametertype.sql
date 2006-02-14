CREATE TABLE ParameterType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 data_type_code NOT NULL,
 measurement_unit_code NOT NULL,
--
 CONSTRAINT pt_pk PRIMARY KEY (id),
 CONSTRAINT pt_uniq UNIQUE (codename),
 CONSTRAINT pt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT pt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT pt_dt_fk FOREIGN KEY (data_type_code)
  REFERENCES DataType (code) ON DELETE CASCADE,
 CONSTRAINT pt_mu_fk FOREIGN KEY (measurement_unit_code)
  REFERENCES MeasurementUnit (code) ON DELETE CASCADE
);

CREATE SEQUENCE ParameterType_seq ORDER;
