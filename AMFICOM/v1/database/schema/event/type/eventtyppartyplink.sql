-- $Id: eventtyppartyplink.sql,v 1.5 2005/08/21 15:11:08 arseniy Exp $

CREATE TABLE EventTypParTypLink (
 event_type_id NOT NULL,
 parameter_type_code NOT NULL,
--
 CONSTRAINT evtpartlnk_uniq
  UNIQUE (event_type_id, parameter_type_code),
 CONSTRAINT evtpartlnk_mntt_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id) ON DELETE CASCADE,
 CONSTRAINT evtpartlnk_part_fk FOREIGN KEY (parameter_type_code)
  REFERENCES ParameterType (code) ON DELETE CASCADE
);
