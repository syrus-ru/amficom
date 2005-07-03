-- $Id: eventsourcelink.sql,v 1.5 2005/06/15 17:03:09 bass Exp $

CREATE TABLE EventSourceLink (
 event_id NOT NULL,
 source_id NOT NULL,
--
 CONSTRAINT evsrclnk_uniq UNIQUE (event_id, source_id),
 CONSTRAINT evsrclnk_event_fk FOREIGN KEY (event_id)
  REFERENCES Event (id) ON DELETE CASCADE,
 CONSTRAINT evsrclnk_evsrc_fk FOREIGN KEY (source_id)
  REFERENCES EventSource (id) ON DELETE CASCADE
);
