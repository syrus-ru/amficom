CREATE TABLE EventSourceLink (
 event_id VARCHAR2(32) NOT NULL,
 source_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT evsrclnk_uniq UNIQUE (event_id, source_id),
 CONSTRAINT evsrclnk_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE,
 CONSTRAINT evsrclnk_evsrc_fk FOREIGN KEY (source_id)
  REFERENCES Event (id) ON DELETE CASCADE
);
