-- $Id: sitenode.sql,v 1.10 2005/09/01 14:06:41 max Exp $

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
 attachment_site_node_id,
--
 CONSTRAINT sitenode_pk PRIMARY KEY (id),
 CONSTRAINT sitenode_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_type_fk FOREIGN KEY (site_node_type_id)
  REFERENCES SiteNodeType (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource(id) ON DELETE CASCADE,
 CONSTRAINT sitenode_att_st_nd_fk FOREIGN KEY (attachment_site_node_id)
  REFERENCES SiteNode(id) ON DELETE CASCADE
);

COMMENT ON TABLE SiteNode IS '$Id: sitenode.sql,v 1.10 2005/09/01 14:06:41 max Exp $';

CREATE SEQUENCE SiteNode_Seq ORDER;
