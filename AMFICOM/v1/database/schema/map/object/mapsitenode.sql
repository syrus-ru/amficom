-- $Id: mapsitenode.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapSiteNode (
 map_id NUMBER(19),
 site_node_id NUMBER(19),
--
 CONSTRAINT mapsn_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapsn_sn_fk FOREIGN KEY (site_node_id)
  REFERENCES SiteNode (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapSiteNode IS '$Id: mapsitenode.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
