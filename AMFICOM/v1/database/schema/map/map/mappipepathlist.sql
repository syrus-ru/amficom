CREATE TABLE MapPipePathList
(
	map_id VARCHAR(32),
	pipe_path_id VARCHAR(32),
--
	CONSTRAINT mppl_pk UNIQUE KEY (pipe_path_id, map_id),
	CONSTRAINT mppl_mse_fk FOREIGN KEY (pipe_path_id)
		REFERENCES MapPipePathElement (id) ON DELETE CASCADE,
	CONSTRAINT mppl_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


