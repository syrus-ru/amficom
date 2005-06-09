CREATE TABLE EquipmentMELink (
 equipment_id NUMBER(19),
 monitored_element_id NUMBER(19),
--
 CONSTRAINT eqmelink_eq_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT eqmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT eqmelink_uniq UNIQUE (equipment_id, monitored_element_id)
);

