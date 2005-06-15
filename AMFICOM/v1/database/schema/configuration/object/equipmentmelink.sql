-- $Id: equipmentmelink.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE EquipmentMELink (
 equipment_id,
 monitored_element_id,
--
 CONSTRAINT eqmelink_eq_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT eqmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT eqmelink_uniq UNIQUE (equipment_id, monitored_element_id)
);
