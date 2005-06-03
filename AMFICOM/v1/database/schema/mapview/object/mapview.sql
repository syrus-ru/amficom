-- $Id: mapview.sql,v 1.5 2005/06/03 11:48:02 bass Exp $

CREATE TABLE MapView (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id VARCHAR2(32),
 name VARCHAR2(128),
 description VARCHAR2(256),
 longitude NUMBER(12, 6),
 latitude NUMBER(12, 6),
 scale NUMBER(12, 6),
 defaultScale NUMBER(12, 6),
 map_id VARCHAR2(32), 
--
 CONSTRAINT mv_pk PRIMARY KEY (id),
 CONSTRAINT mv_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mv_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mv_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
 CONSTRAINT mv_map_fk FOREIGN KEY (map_id)
  REFERENCES Map (id) ON DELETE CASCADE
);

COMMENT ON TABLE MapView IS '$Id: mapview.sql,v 1.5 2005/06/03 11:48:02 bass Exp $';

CREATE SEQUENCE MapView_Seq ORDER;
