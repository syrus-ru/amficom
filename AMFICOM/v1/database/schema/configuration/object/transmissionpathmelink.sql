CREATE TABLE TransmissionPathMELink (
 transmission_path_id VARCHAR2(32),
 monitored_element_id VARCHAR2(32),
--
 CONSTRAINT tpathmelink_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT tpathmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);

