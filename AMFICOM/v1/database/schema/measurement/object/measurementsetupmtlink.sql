CREATE TABLE MeasurementSetupMTLink (
 measurement_setup_id VARCHAR2(32) NOT NULL,
 measurement_type_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mntspmtlink_mntsetup_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmtlink_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType(id) ON DELETE CASCADE
);
