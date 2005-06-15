-- $Id: transmissionpathmelink.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

CREATE TABLE TransmissionPathMELink (
 transmission_path_id,
 monitored_element_id,
--
 CONSTRAINT tpathmelink_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT tpathmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT tpathmelink_uniq UNIQUE (transmission_path_id, monitored_element_id)
);

