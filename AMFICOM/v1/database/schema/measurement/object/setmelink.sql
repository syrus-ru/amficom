-- $Id: setmelink.sql,v 1.6 2005/06/15 07:50:18 bass Exp $

CREATE TABLE SetMELink (
 set_id NUMBER(19) NOT NULL,
 monitored_element_id NUMBER(19) NOT NULL,
--
 CONSTRAINT setmelink_set_fk FOREIGN KEY (set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
