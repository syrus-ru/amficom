CREATE TABLE MeasurementSetupTestLink (
 test_id NUMBER(20, 0) NOT NULL,
 measurement_setup_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT mntsptlnk_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mntsptlnk_mntsp_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup ON DELETE CASCADE ENABLE
);
