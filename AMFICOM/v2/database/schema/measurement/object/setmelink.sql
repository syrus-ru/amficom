CREATE TABLE SetMELink (
 set_id NUMBER(20, 0) NOT NULL,
 monitored_element_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT setmelink_sett_fk FOREIGN KEY (set_id)
  REFERENCES Sett (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE ENABLE
);
