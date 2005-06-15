-- $Id: measurementsetupmtlink.sql,v 1.3 2005/06/15 07:50:18 bass Exp $

CREATE TABLE MeasurementSetupMTLink (
 measurement_setup_id NUMBER(19) NOT NULL,
 measurement_type_id NUMBER(19) NOT NULL,
--
 CONSTRAINT mntspmtlink_mntsetup_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmtlink_mnttype_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType(id) ON DELETE CASCADE
);
