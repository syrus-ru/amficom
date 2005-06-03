-- $Id: mapviewscheme.sql,v 1.3 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapViewScheme (
 mapview_id VARCHAR2(32) NOT NULL,
 scheme_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mvs_mapview_fk FOREIGN KEY (mapview_id)
  REFERENCES MapView (id) ON DELETE CASCADE,
 CONSTRAINT mvs_scheme_fk FOREIGN KEY (scheme_id)
  REFERENCES "Scheme" (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapViewScheme IS '$Id: mapviewscheme.sql,v 1.3 2005/06/03 11:48:02 bass Exp $';
