CREATE TABLE MapViewScheme (
 mapview_id VARCHAR2(32),
 scheme_id VARCHAR2(32),
--
 CONSTRAINT mvs_mapview_fk FOREIGN KEY (mapview_id)
  REFERENCES MapView (id) ON DELETE CASCADE
);
