-- $Id: maptopologicalnode.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapTopologicalNode (
 map_id,
 topological_node_id,
--
 CONSTRAINT maptn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT maptn_tn_fk FOREIGN KEY (topological_node_id)
  REFERENCES TopologicalNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapTopologicalNode IS '$Id: maptopologicalnode.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
