-- $Id: collphlink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $

CREATE TABLE CollPhLink (
 collector_id VARCHAR2(32),
 physical_link_id VARCHAR2(32),
--
 CONSTRAINT collphlink_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE,
 CONSTRAINT collphlink_phln_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE CollPhLink IS '$Id: collphlink.sql,v 1.2 2005/06/03 11:48:02 bass Exp $';
