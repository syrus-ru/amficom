CREATE TABLE MapViewSchemeList
(
	scheme_id VARCHAR(32),
	map_view_id VARCHAR(32),
--
	CONSTRAINT mvsl_pk UNIQUE KEY (map_view_id, scheme_id),
	CONSTRAINT mvsl_scheme_fk FOREIGN KEY (scheme_id)
		REFERENCES Scheme (id) ON DELETE CASCADE,
	CONSTRAINT mvsl_mv_fk FOREIGN KEY (map_view_id)
		REFERENCES MapView (id) ON DELETE CASCADE
);


