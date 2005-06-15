-- $Id: transmissionpathmelink.sql,v 1.5 2005/06/15 07:50:18 bass Exp $

CREATE TABLE TransmissionPathMELink (
 transmission_path_id NUMBER(19),
 monitored_element_id NUMBER(19),
--
 CONSTRAINT tpathmelink_tpath_fk FOREIGN KEY (transmission_path_id)
  REFERENCES TransmissionPath (id) ON DELETE CASCADE,
 CONSTRAINT tpathmelink_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT tpathmelink_uniq UNIQUE (transmission_path_id, monitored_element_id)
);

