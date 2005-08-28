-- $Id: transmissionpathmelink.sql,v 1.1 2005/08/28 14:29:24 arseniy Exp $

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

