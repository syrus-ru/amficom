CREATE TABLE MapPhysicalLinkList
(
	map_id VARCHAR(32),
	physical_link_id VARCHAR(32),
--
	CONSTRAINT mpll_pk UNIQUE KEY (physical_link_id, map_id),
	CONSTRAINT mpll_mple_fk FOREIGN KEY (physical_link_id)
		REFERENCES MapPhysicalLinkElement (id) ON DELETE CASCADE,
	CONSTRAINT mpll_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


