-- $Id: parametersetmelink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE ParameterSetMELink (
 set_id NOT NULL,
 monitored_element_id NOT NULL,
--
 CONSTRAINT setmelink_set_fk FOREIGN KEY (set_id)
  REFERENCES ParameterSet (id) ON DELETE CASCADE,
 CONSTRAINT setmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE
);
