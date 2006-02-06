-- $Id: mnttyppartyplink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE MntTypParTypLink (
 measurement_type_code NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT mnttpartlnk_uniq
  UNIQUE (measurement_type_code, parameter_type_code),
 CONSTRAINT mnttpartlnk_mnttyp_fk FOREIGN KEY (measurement_type_code)
  REFERENCES MeasurementType (code) ON DELETE CASCADE,
 CONSTRAINT mnttpartlnk_pt_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT mnttpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
 )
);
