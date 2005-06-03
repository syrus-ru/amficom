-- $Id: mapmark.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapMark (
 map_id VARCHAR2(32),
 mark_id VARCHAR2(32),
--
 CONSTRAINT mapm_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapm_mark_fk FOREIGN KEY (mark_id)
  REFERENCES Mark (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapMark IS '$Id: mapmark.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
