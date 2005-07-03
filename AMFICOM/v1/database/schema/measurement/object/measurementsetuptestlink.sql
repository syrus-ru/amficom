-- $Id: measurementsetuptestlink.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MeasurementSetupTestLink (
 test_id NOT NULL,
 measurement_setup_id NOT NULL,
--
 CONSTRAINT mntsptlnk_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT mntsptlnk_mntsp_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup ON DELETE CASCADE
);
