CREATE TABLE EquipmentMELink (
 equipment_id VARCHAR2(32),
 monitored_element_id VARCHAR2(32),
--
 CONSTRAINT eqmelink_eq_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT eqmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

