CREATE TABLE MntTypParTypLink (
 measurement_type_id VARCHAR2(32) NOT NULL,
 parameter_type_id VARCHAR2(32) NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
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

