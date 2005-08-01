CREATE TABLE MapExtNodeLink (
 map_id,
 ext_node_link_id,
--
 CONSTRAINT mapexnl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapexnl_exnl_fk FOREIGN KEY (ext_node_link_id)
  REFERENCES NodeLink (id) ON DELETE CASCADE
);