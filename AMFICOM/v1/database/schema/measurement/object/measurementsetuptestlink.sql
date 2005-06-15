-- $Id: measurementsetuptestlink.sql,v 1.5 2005/06/15 07:50:18 bass Exp $

CREATE TABLE MeasurementSetupTestLink (
 test_id NUMBER(19) NOT NULL,
 measurement_setup_id NUMBER(19) NOT NULL,
--
 CONSTRAINT mntsptlnk_test_fk FOREIGN KEY (test_id)
  REFERENCES Test (id) ON DELETE CASCADE,
 CONSTRAINT mntsptlnk_mntsp_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup ON DELETE CASCADE
);
