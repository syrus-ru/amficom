-- $Id: mapviewscheme.sql,v 1.5 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MapViewScheme (
 mapview_id NOT NULL,
 scheme_id NOT NULL,
--
 CONSTRAINT mvs_mapview_fk FOREIGN KEY (mapview_id)
  REFERENCES MapView (id) ON DELETE CASCADE,
 CONSTRAINT mvs_scheme_fk FOREIGN KEY (scheme_id)
  REFERENCES Scheme (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapViewScheme IS '$Id: mapviewscheme.sql,v 1.5 2005/06/15 17:03:09 bass Exp $';
