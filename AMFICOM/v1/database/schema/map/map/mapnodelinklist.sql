CREATE TABLE MapNodeLinkList
(
	map_id VARCHAR(32),
	node_link_id VARCHAR(32),
--
	CONSTRAINT mnll_pk UNIQUE KEY (node_link_id, map_id),
	CONSTRAINT mnll_mnle_fk FOREIGN KEY (node_link_id)
		REFERENCES MapNodeLinkElement (id) ON DELETE CASCADE,
	CONSTRAINT mnll_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


