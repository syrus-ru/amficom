CREATE TABLE CollPhLink (
 collector_id VARCHAR2(32),
 physical_link_id VARCHAR2(32),
--
 CONSTRAINT collphlink_coll_fk FOREIGN KEY (collector_id)
  REFERENCES Collector (id) ON DELETE CASCADE,
 CONSTRAINT collphlink_phln_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);
