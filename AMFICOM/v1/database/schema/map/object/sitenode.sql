-- $Id: sitenode.sql,v 1.9 2005/06/15 17:03:09 bass Exp $

CREATE TABLE SiteNode (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 longitude NUMBER(12, 6),
 latiude NUMBER(12, 6),
 image_id NOT NULL,
 site_node_type_id NOT NULL,
 city VARCHAR2(128 CHAR),
 street VARCHAR2(128 CHAR),
 building VARCHAR2(128 CHAR),
--
 CONSTRAINT sitenode_pk PRIMARY KEY (id),
 CONSTRAINT sitenode_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_type_fk FOREIGN KEY (site_node_type_id)
  REFERENCES SiteNodeType (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource(id) ON DELETE CASCADE
);

COMMENT ON TABLE SiteNode IS '$Id: sitenode.sql,v 1.9 2005/06/15 17:03:09 bass Exp $';

CREATE SEQUENCE SiteNode_Seq ORDER;
