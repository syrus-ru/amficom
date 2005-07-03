-- $Id: eventtyppartyplink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE EventTypParTypLink (
 event_type_id NOT NULL,
 parameter_type_id NOT NULL,
--
 CONSTRAINT evtpartlnk_uniq
  UNIQUE (event_type_id, parameter_type_id),
 CONSTRAINT evtpartlnk_mntt_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id) ON DELETE CASCADE,
 CONSTRAINT evtpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE
);
