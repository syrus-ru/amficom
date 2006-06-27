CREATE TABLE MntTypParTypLink (
 measurement_type_id NUMBER(20, 0) NOT NULL,
 parameter_type_id NUMBER(20, 0) NOT NULL,
 parameter_mode VARCHAR2(3) NOT NULL,
 CONSTRAINT mnttpartlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mnttpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mnttpartlnk_chk CHECK (parameter_mode = 'IN'
  OR parameter_mode = 'OUT')
);
