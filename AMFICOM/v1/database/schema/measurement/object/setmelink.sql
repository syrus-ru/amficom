CREATE TABLE SetMELink (
 set_id Identifier NOT NULL,
 monitored_element_id Identifier NOT NULL,
--
 CONSTRAINT setmelink_sett_fk FOREIGN KEY (set_id)
  REFERENCES Sett (id) ON DELETE CASCADE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
