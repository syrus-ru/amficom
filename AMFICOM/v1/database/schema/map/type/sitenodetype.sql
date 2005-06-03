-- $Id: sitenodetype.sql,v 1.4 2005/06/03 11:48:02 bass Exp $

CREATE TABLE SiteNodeType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 name VARCHAR2(128),
 description VARCHAR2(256),
 image_id VARCHAR2(32) NOT NULL,
 topological NUMBER(1),
--
 CONSTRAINT sitnodetype_pk PRIMARY KEY (id),
 CONSTRAINT sitnodetype_uniq UNIQUE (codename),
 CONSTRAINT sitnodetype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT sitnodetype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

COMMENT ON TABLE SiteNodeType IS '$Id: sitenodetype.sql,v 1.4 2005/06/03 11:48:02 bass Exp $';

CREATE SEQUENCE SiteNodeType_Seq ORDER;
