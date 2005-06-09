-- $Id: collphlink.sql,v 1.3 2005/06/09 14:40:11 max Exp $

CREATE TABLE CollPhLink (
 collector_id NUMBER(19),
 physical_link_id NUMBER(19),
--
 CONSTRAINT collphlink_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE,
 CONSTRAINT collphlink_phln_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE CollPhLink IS '$Id: collphlink.sql,v 1.3 2005/06/09 14:40:11 max Exp $';
