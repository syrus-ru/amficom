-- $Id: mapmark.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapMark (
 map_id NUMBER(19),
 mark_id NUMBER(19),
--
 CONSTRAINT mapm_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapm_mark_fk FOREIGN KEY (mark_id)
  REFERENCES Mark (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapMark IS '$Id: mapmark.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
