-- $Id: mapnodelink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapNodeLink (
 map_id VARCHAR2(32),
 node_link_id VARCHAR2(32),
--
 CONSTRAINT mapnl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapnl_nl_fk FOREIGN KEY (node_link_id)
  REFERENCES NodeLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapNodeLink IS '$Id: mapnodelink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
