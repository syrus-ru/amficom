-- $Id: collphlink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE CollPhLink (
 collector_id,
 physical_link_id,
--
 CONSTRAINT collphlink_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE,
 CONSTRAINT collphlink_phln_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE CollPhLink IS '$Id: collphlink.sql,v 1.4 2005/06/15 17:03:09 bass Exp $';
