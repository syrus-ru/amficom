CREATE TABLE MntPortTypMntTypLink (
 measurement_port_type_id NOT NULL,
 measurement_type_code NOT NULL,
--
 CONSTRAINT mptypmnttyplink_mpttyp_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE,
 CONSTRAINT mptypmnttyplink_mnttyp_fk FOREIGN KEY (measurement_type_code)
  REFERENCES MeasurementType (code) ON DELETE CASCADE
);
