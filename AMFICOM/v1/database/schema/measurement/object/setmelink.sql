CREATE TABLE SetMELink (
 set_id VARCHAR2(32) NOT NULL,
 monitored_element_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT setmelink_set_fk FOREIGN KEY (set_id)
  REFERENCES "Set" (id) ON DELETE CASCADE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
