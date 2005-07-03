-- $Id: setmelink.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

CREATE TABLE SetMELink (
 set_id NOT NULL,
 monitored_element_id NOT NULL,
--
 CONSTRAINT setmelink_set_fk FOREIGN KEY (set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
