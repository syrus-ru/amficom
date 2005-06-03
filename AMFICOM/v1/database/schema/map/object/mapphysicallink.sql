-- $Id: mapphysicallink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapPhysicalLink (
 map_id VARCHAR2(32),
 physical_link_id VARCHAR2(32),
--
 CONSTRAINT mappl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mappl_pl_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapPhysicalLink IS '$Id: mapphysicallink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
