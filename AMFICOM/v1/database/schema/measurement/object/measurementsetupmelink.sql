CREATE TABLE MeasurementSetupMELink (
 measurement_setup_id VARCHAR2(32) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mntspmelink_mntsetup_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
