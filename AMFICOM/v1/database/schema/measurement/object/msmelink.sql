-- $Id: msmelink.sql,v 1.1.2.1 2006/02/15 19:42:55 arseniy Exp $

CREATE TABLE MSMELink (
 measurement_setup_id NOT NULL,
 monitored_element_id NOT NULL,
--
 CONSTRAINT msmel_uniq UNIQUE (measurement_setup_id, monitored_element_id),
 CONSTRAINT msmel_actmpl_fk FOREIGN KEY (measurement_setup_id)
  REFERENCES MeasurementSetup (id) ON DELETE CASCADE,
 CONSTRAINT msmel_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement(id) ON DELETE CASCADE
);
