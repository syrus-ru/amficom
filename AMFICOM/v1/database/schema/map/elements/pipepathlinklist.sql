CREATE TABLE MapPipePathLinkList
(
	physical_link_id VARCHAR(32),
	map_id VARCHAR(32),
--
	CONSTRAINT ppll_pk UNIQUE KEY (physical_link_id, pipe_path_id),
	CONSTRAINT ppll_mple_fk FOREIGN KEY (physical_link_id)
		REFERENCES MapPhysicalLinkElement (id) ON DELETE CASCADE,
	CONSTRAINT ppll_mppe_fk FOREIGN KEY (pipe_path_id)
		REFERENCES MapPipePathElement (id) ON DELETE CASCADE
);


