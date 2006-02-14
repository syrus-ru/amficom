-- $Id: mapmaplink.sql,v 1.2.2.1 2006/02/14 11:06:59 arseniy Exp $

CREATE TABLE MapMapLink (
 map_id,
 child_map_id,
--
 CONSTRAINT map_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT map_cdmap_fk FOREIGN KEY (child_map_id)
  REFERENCES Map (id) ON DELETE CASCADE
);
