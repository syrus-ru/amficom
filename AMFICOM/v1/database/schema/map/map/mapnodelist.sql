CREATE TABLE MapPhysicalNodeList
(
	map_id VARCHAR(32),
	physical_node_id VARCHAR(32),
--
	CONSTRAINT mpnl_pk UNIQUE KEY (physical_node_id, map_id),
	CONSTRAINT mpnl_mpne_fk FOREIGN KEY (physical_node_id)
		REFERENCES MapPhysicalNodeElement (id) ON DELETE CASCADE,
	CONSTRAINT mpnl_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


