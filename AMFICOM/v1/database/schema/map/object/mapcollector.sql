-- $Id: mapcollector.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapCollector (
 map_id,
 collector_id,
--
 CONSTRAINT mapc_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapc_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapCollector IS '$Id: mapcollector.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
