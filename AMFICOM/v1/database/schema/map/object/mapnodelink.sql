-- $Id: mapnodelink.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapNodeLink (
 map_id NUMBER(19),
 node_link_id NUMBER(19),
--
 CONSTRAINT mapnl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mapnl_nl_fk FOREIGN KEY (node_link_id)
  REFERENCES NodeLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapNodeLink IS '$Id: mapnodelink.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
