-- $Id: mapview.sql,v 1.7 2005/06/15 09:40:34 bass Exp $

CREATE TABLE MapView (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 longitude NUMBER(12, 6),
 latitude NUMBER(12, 6),
 scale NUMBER(12, 6),
 defaultScale NUMBER(12, 6),
 map_id NUMBER(19), 
--
 CONSTRAINT mv_pk PRIMARY KEY (id),
 CONSTRAINT mv_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mv_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mv_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
 CONSTRAINT mv_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapView IS '$Id: mapview.sql,v 1.7 2005/06/15 09:40:34 bass Exp $';

CREATE SEQUENCE MapView_Seq ORDER;
