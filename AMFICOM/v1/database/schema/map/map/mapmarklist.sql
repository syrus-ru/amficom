CREATE TABLE MapMarkList
(
	map_id VARCHAR(32),
	mark_id VARCHAR(32),
--
	CONSTRAINT mml_pk UNIQUE KEY (mark_id, map_id),
	CONSTRAINT mml_mme_fk FOREIGN KEY (mark_id)
		REFERENCES MapMarkElement (id) ON DELETE CASCADE,
	CONSTRAINT mml_map_fk FOREIGN KEY (map_id)
		REFERENCES Map (id) ON DELETE CASCADE
);


