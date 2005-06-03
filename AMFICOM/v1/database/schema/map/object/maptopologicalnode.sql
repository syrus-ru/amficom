-- $Id: maptopologicalnode.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapTopologicalNode (
 map_id VARCHAR2(32),
 topological_node_id VARCHAR2(32),
--
 CONSTRAINT maptn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT maptn_tn_fk FOREIGN KEY (topological_node_id)
  REFERENCES TopologicalNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapTopologicalNode IS '$Id: maptopologicalnode.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
