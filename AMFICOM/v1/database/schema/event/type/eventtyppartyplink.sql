-- $Id: eventtyppartyplink.sql,v 1.5.2.1 2006/02/14 11:09:11 arseniy Exp $

CREATE TABLE EventTypParTypLink (
 event_type_id NOT NULL,
 parameter_type_id NOT NULL,
--
 CONSTRAINT etptl_uniq
  UNIQUE (event_type_id, parameter_type_id),
 CONSTRAINT etptl_et_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id) ON DELETE CASCADE,
 CONSTRAINT etptl_pt_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE
);
