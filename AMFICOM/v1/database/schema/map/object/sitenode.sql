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
 CONSTRAINT sitnode_pk PRIMARY KEY (id),
 CONSTRAINT sitnode_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT sitnode_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT sitnode_type_modifier_fk FOREIGN KEY (site_node_type_id)
  REFERENCES SiteNodeType (id) ON DELETE CASCADE
);

CREATE SEQUENCE sitenode_seq ORDER;
