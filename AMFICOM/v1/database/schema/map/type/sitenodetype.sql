-- $Id: sitenodetype.sql,v 1.8 2005/06/15 17:03:09 bass Exp $

CREATE TABLE SiteNodeType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 image_id NOT NULL,
 topological NUMBER(1),
--
 CONSTRAINT sitnodetype_pk PRIMARY KEY (id),
 CONSTRAINT sitnodetype_uniq UNIQUE (codename),
 CONSTRAINT sitnodetype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitnodetype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitnodetype_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE
);

COMMENT ON TABLE SiteNodeType IS '$Id: sitenodetype.sql,v 1.8 2005/06/15 17:03:09 bass Exp $';

CREATE SEQUENCE SiteNodeType_Seq ORDER;
