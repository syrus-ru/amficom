-- $Id: mapsitenode.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapSiteNode (
 map_id VARCHAR2(32),
 site_node_id VARCHAR2(32),
--
 CONSTRAINT mapsn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapsn_sn_fk FOREIGN KEY (site_node_id)
  REFERENCES SiteNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapSiteNode IS '$Id: mapsitenode.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
