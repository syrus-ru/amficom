CREATE TABLE MapTopologicalNode (
 map_id VARCHAR2(32),
 topological_node_id VARCHAR2(32),
--
 CONSTRAINT maptn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT maptn_tn_fk FOREIGN KEY (topological_node_id)
  REFERENCES TopologicalNode (id) ON DELETE CASCADE
);
