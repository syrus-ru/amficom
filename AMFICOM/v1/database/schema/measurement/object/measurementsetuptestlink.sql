CREATE TABLE MeasurementSetupTestLink (
 test_id VARCHAR2(32) NOT NULL,
 measurement_setup_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mntsptlnk_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT mntsptlnk_mntsp_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup ON DELETE CASCADE
);
