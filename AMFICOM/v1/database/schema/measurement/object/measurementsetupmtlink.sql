-- $Id: measurementsetupmtlink.sql,v 1.5 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE MeasurementSetupMTLink (
 measurement_setup_id NOT NULL,
 measurement_type_code NOT NULL,
--
 CONSTRAINT mntspmtlink_mntsetup_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmtlink_mnttype_fk FOREIGN KEY (measurement_type_code)
  REFERENCES MeasurementType(code) ON DELETE CASCADE
);
