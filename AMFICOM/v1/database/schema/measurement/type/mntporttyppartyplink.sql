CREATE TABLE MntPortTypParTypLink (
 measurement_port_type_id NOT NULL,
 parameter_type_code NOT NULL,
--
 CONSTRAINT mptyppartyplink_mpttyp_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE,
 CONSTRAINT mptyppartyplink_partyp_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE
);
