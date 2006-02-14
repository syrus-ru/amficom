-- $Id: transmissionpathmelink.sql,v 1.1.2.1 2006/02/14 09:55:28 arseniy Exp $

CREATE TABLE TransmissionPathMELink (
 transmission_path_id,
 monitored_element_id,
--
 CONSTRAINT tpml_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT tpml_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT tpml_uniq UNIQUE (transmission_path_id, monitored_element_id)
);

