-- $Id: mapcollector.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapCollector (
 map_id VARCHAR2(32),
 collector_id VARCHAR2(32),
--
 CONSTRAINT mapc_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapc_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapCollector IS '$Id: mapcollector.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
