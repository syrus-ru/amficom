CREATE TABLE TransmissionPathMELink (
 transmission_path_id Identifier,
 monitored_element_id Identifier,
--
 CONSTRAINT tpmelink_tp_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE
 CONSTRAINT tpmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

