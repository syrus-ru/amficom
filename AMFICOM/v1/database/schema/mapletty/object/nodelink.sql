CREATE TABLE NodeLink (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(128),
 physical_link_id VARCHAR2(32),
 start_node_id VARCHAR2(32),
 end_node_id VARCHAR2(32), 
 length NUMBER(12,6),
--
 CONSTRAINT nodelink_pk PRIMARY KEY (id),
 CONSTRAINT nodelink_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT nodelink_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT nodelink_phlink_type_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE,
);

CREATE SEQUENCE nodelink_seq ORDER;
