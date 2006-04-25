-- $Id: mapmaplibrarylink.sql,v 1.2 2006/04/25 07:50:16 arseniy Exp $

CREATE TABLE MapMapLibraryLink (
 map_id,
 map_library_id,
--
 CONSTRAINT mmliblk_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE,
 CONSTRAINT mmliblk_map_lib_fk FOREIGN KEY (map_library_id)
  REFERENCES MapLibrary (id) ON DELETE CASCADE
);
