-- $Id: sitenodetype.sql,v 1.12 2005/08/04 15:22:26 max Exp $

CREATE TABLE SiteNodeType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 sort NUMBER(1) NOT NULL,
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(64 CHAR),
 description VARCHAR2(256 CHAR),
 image_id NOT NULL,
 topological NUMBER(1) NOT NULL,
 map_library_id,
--
 CONSTRAINT sitnodetype_pk PRIMARY KEY (id),
 CONSTRAINT sitnodetype_uniq UNIQUE (codename),
 CONSTRAINT sitnodetype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitnodetype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitnodetype_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE,
  CONSTRAINT sitnodetype_ml_id_fk FOREIGN KEY (map_library_id)
  REFERENCES MapLibrary (id) ON DELETE CASCADE
);

COMMENT ON TABLE SiteNodeType IS '$Id: sitenodetype.sql,v 1.12 2005/08/04 15:22:26 max Exp $';

CREATE SEQUENCE SiteNodeType_Seq ORDER;
