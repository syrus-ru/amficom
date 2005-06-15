-- $Id: sitenode.sql,v 1.7 2005/06/15 07:50:18 bass Exp $

CREATE TABLE SiteNode (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 longitude NUMBER(12, 6),
 latiude NUMBER(12, 6),
 image_id NUMBER(19) NOT NULL,
 site_node_type_id NUMBER(19) NOT NULL,
 city VARCHAR2(128),
 street VARCHAR2(128),
 building VARCHAR2(128),
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

COMMENT ON TABLE SiteNode IS '$Id: sitenode.sql,v 1.7 2005/06/15 07:50:18 bass Exp $';

CREATE SEQUENCE SiteNode_Seq ORDER;
