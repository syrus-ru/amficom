-- $Id: mapmark.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapMark (
 map_id,
 mark_id,
--
 CONSTRAINT mapm_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapm_mark_fk FOREIGN KEY (mark_id)
  REFERENCES Mark (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapMark IS '$Id: mapmark.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
