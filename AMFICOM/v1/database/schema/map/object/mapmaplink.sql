CREATE TABLE MapMapLink (
 map_id,
 child_map_id,
--
 CONSTRAINT mmliblk_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mmliblk_cdmap_fk FOREIGN KEY (child_map_id)
  REFERENCES Map (id) ON DELETE CASCADE
);