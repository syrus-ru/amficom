-- $Id: mnttypmeasporttyplink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MntTypMeasPortTypLink (
 measurement_type_id NOT NULL,
 measurement_port_type_id NOT NULL,
--
 CONSTRAINT mnttmeasportlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttmeasportlnk_part_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE
);
