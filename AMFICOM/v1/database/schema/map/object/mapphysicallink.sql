-- $Id: mapphysicallink.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapPhysicalLink (
 map_id NUMBER(19),
 physical_link_id NUMBER(19),
--
 CONSTRAINT mappl_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mappl_pl_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapPhysicalLink IS '$Id: mapphysicallink.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
