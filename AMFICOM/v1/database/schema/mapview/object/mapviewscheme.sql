-- $Id: mapviewscheme.sql,v 1.2 2005/02/21 08:52:51 bass Exp $

CREATE TABLE MapViewScheme (
 mapview_id VARCHAR2(32) NOT NULL,
 scheme_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mvs_mapview_fk FOREIGN KEY (mapview_id)
  REFERENCES MapView (id) ON DELETE CASCADE,
 CONSTRAINT mvs_scheme_fk FOREIGN KEY (scheme_id)
  REFERENCES "Scheme" (id) ON DELETE CASCADE
);
