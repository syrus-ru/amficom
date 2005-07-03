-- $Id: measurementsetupmelink.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MeasurementSetupMELink (
 measurement_setup_id NOT NULL,
 monitored_element_id NOT NULL,
--
 CONSTRAINT mntspmelink_mntsetup_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT mntspmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
