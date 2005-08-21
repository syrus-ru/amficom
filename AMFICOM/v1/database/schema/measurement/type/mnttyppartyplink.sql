-- $Id: mnttyppartyplink.sql,v 1.10 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE MntTypParTypLink (
 measurement_type_id NOT NULL,
 parameter_type_code NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT mnttpartlnk_uniq
  UNIQUE (measurement_type_id, parameter_type_code),
 CONSTRAINT mnttpartlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttpartlnk_part_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE,
--
 CONSTRAINT mnttpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
 )
);
