-- $Id: mapphysicallink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapPhysicalLink (
 map_id,
 physical_link_id,
--
 CONSTRAINT mappl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mappl_pl_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapPhysicalLink IS '$Id: mapphysicallink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
