CREATE TABLE SiteNode (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 longitude NUMBER(12, 6),
 latiude NUMBER(12, 6),
 image_id VARCHAR2(32) NOT NULL,
 site_node_type_id VARCHAR2(32) NOT NULL,
 city VARCHAR2(128),
 street VARCHAR2(128),
 building VARCHAR2(128),
--
 CONSTRAINT sitenode_pk PRIMARY KEY (id),
 CONSTRAINT sitenode_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT sitenode_type_fk FOREIGN KEY (site_node_type_id)
  REFERENCES SiteNodeType (id) ON DELETE CASCADE
);

CREATE SEQUENCE sitenode_seq ORDER;
