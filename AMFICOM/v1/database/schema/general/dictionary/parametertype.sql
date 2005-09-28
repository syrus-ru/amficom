-- $Id: parametertype.sql,v 1.2 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE ParameterType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
 data_type_code NOT NULL,
 measurement_unit_code NOT NULL,
 description VARCHAR2(64 CHAR),
--
 CONSTRAINT pt_pk PRIMARY KEY (code),
 CONSTRAINT pt_uniq UNIQUE (codename),
 CONSTRAINT pt_dt_fk FOREIGN KEY (data_type_code)
  REFERENCES DataType(code) ON DELETE CASCADE,
 CONSTRAINT pt_mu_fk FOREIGN KEY (measurement_unit_code)
  REFERENCES MeasurementUnit(code) ON DELETE CASCADE
);
