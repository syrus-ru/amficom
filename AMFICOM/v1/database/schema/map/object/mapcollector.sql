-- $Id: mapcollector.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapCollector (
 map_id NUMBER(19),
 collector_id NUMBER(19),
--
 CONSTRAINT mapc_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapc_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapCollector IS '$Id: mapcollector.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
