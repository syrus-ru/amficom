CREATE TABLE MapSiteList
(
	map_id VARCHAR(32),
	site_id VARCHAR(32),
--
	CONSTRAINT msl_pk UNIQUE KEY (site_id, map_id),
	CONSTRAINT msl_mse_fk FOREIGN KEY (site_id)
		REFERENCES MapSiteElement (id) ON DELETE CASCADE,
	CONSTRAINT msl_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


