-- $Id: mapnodelink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapNodeLink (
 map_id,
 node_link_id,
--
 CONSTRAINT mapnl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapnl_nl_fk FOREIGN KEY (node_link_id)
  REFERENCES NodeLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapNodeLink IS '$Id: mapnodelink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
