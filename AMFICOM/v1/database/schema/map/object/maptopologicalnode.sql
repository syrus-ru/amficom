-- $Id: maptopologicalnode.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapTopologicalNode (
 map_id NUMBER(19),
 topological_node_id NUMBER(19),
--
 CONSTRAINT maptn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT maptn_tn_fk FOREIGN KEY (topological_node_id)
  REFERENCES TopologicalNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapTopologicalNode IS '$Id: maptopologicalnode.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
