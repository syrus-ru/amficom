CREATE TABLE MeasurementSetupMELink (
 measurement_setup_id Identifier NOT NULL,
 monitored_element_id Identifier NOT NULL,
--
 CONSTRAINT mntspmelink_mntsp_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
