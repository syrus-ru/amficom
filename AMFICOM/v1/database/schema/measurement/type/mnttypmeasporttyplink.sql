CREATE TABLE MntTypMeasPortTypLink (
 measurement_type_id VARCHAR2(32) NOT NULL,
 measurement_port_type_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mnttmeasportlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttmeasportlnk_part_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE
);

