-- $Id: mapextnodelink.sql,v 1.4 2006/04/25 07:50:16 arseniy Exp $

CREATE TABLE MapExtNodeLink (
 map_id,
 ext_site_node_id,
--
 CONSTRAINT mapextnode_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapextnode_extnode_fk FOREIGN KEY (ext_site_node_id)
  REFERENCES SiteNode (id) ON DELETE CASCADE
);
