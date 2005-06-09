-- $Id: mapviewscheme.sql,v 1.4 2005/06/09 14:40:11 max Exp $

CREATE TABLE MapViewScheme (
 mapview_id NUMBER(19) NOT NULL,
 scheme_id NUMBER(19) NOT NULL,
--
 CONSTRAINT mvs_mapview_fk FOREIGN KEY (mapview_id)
  REFERENCES MapView (id) ON DELETE CASCADE,
 CONSTRAINT mvs_scheme_fk FOREIGN KEY (scheme_id)
  REFERENCES Scheme (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapViewScheme IS '$Id: mapviewscheme.sql,v 1.4 2005/06/09 14:40:11 max Exp $';
