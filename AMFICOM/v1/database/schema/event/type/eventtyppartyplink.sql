CREATE TABLE EventTypParTypLink (
 event_type_id VARCHAR2(32) NOT NULL,
 parameter_type_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT evtpartlnk_uniq
  UNIQUE (event_type_id, parameter_type_id),
 CONSTRAINT evtpartlnk_mntt_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id) ON DELETE CASCADE,
 CONSTRAINT evtpartlnk_part_fk FOREIGN KEY (parameter_type_id)
  REFERENCES ParameterType (id) ON DELETE CASCADE
);
