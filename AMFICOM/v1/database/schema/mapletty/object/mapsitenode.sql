CREATE TABLE MapSiteNode (
 map_id VARCHAR2(32),
 site_node_id VARCHAR2(32),
--
 CONSTRAINT mapsn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapsn_sn_fk FOREIGN KEY (site_node_id)
  REFERENCES SiteNode (id) ON DELETE CASCADE
);
