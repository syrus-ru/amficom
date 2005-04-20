CREATE TABLE UserEventTypeLink (
 user_id VARCHAR2(32) NOT NULL,
 event_type_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT uetlnk_user_fk FOREIGN KEY (user_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT uetlnk_evtype_fk FOREIGN KEY (event_type_id)
  REFERENCES EventType (id)  ON DELETE CASCADE,
 CONSTRAINT uetlnk_uniq UNIQUE (user_id, event_type_id)
);
