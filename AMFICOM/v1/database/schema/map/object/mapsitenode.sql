-- $Id: mapsitenode.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapSiteNode (
 map_id,
 site_node_id,
--
 CONSTRAINT mapsn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapsn_sn_fk FOREIGN KEY (site_node_id)
  REFERENCES SiteNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapSiteNode IS '$Id: mapsitenode.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
