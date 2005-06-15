-- $Id: mnttyppartyplink.sql,v 1.8 2005/06/15 09:40:35 bass Exp $

CREATE TABLE MntTypParTypLink (
 measurement_type_id NUMBER(19) NOT NULL,
 parameter_type_id NUMBER(19) NOT NULL,
 parameter_mode VARCHAR2(3 CHAR) NOT NULL,
--
 CONSTRAINT mnttpartlnk_uniq
  UNIQUE (measurement_type_id, parameter_type_id),
 CONSTRAINT mnttpartlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE,
--
 CONSTRAINT mnttpartlnk_chk CHECK (
  parameter_mode IN (
   'IN',
   'OUT')
 )
);
