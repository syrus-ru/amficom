-- $Id: mnttypmeasporttyplink.sql,v 1.3 2005/06/15 07:50:19 bass Exp $

CREATE TABLE MntTypMeasPortTypLink (
 measurement_type_id NUMBER(19) NOT NULL,
 measurement_port_type_id NUMBER(19) NOT NULL,
--
 CONSTRAINT mnttmeasportlnk_mntt_fk FOREIGN KEY (measurement_type_id)
  REFERENCES MeasurementType (id) ON DELETE CASCADE,
 CONSTRAINT mnttmeasportlnk_part_fk FOREIGN KEY (measurement_port_type_id)
  REFERENCES MeasurementPortType (id) ON DELETE CASCADE
);
