CREATE TABLE PhysicalLink (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 physical_link_type_id VARCHAR2(32) NOT NULL,
 city VARCHAR2(128),
 street VARCHAR2(128),
 building VARCHAR2(128),
 dimension_x NUMBER(12),
 dimension_y NUMBER(12),
 topLeft NUMBER(1),
 start_node_id VARCHAR2(32),
 end_node_id VARCHAR2(32),
--
 CONSTRAINT phlink_pk PRIMARY KEY (id),
 CONSTRAINT phlink_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT phlink_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT phlink_type_modifier_fk FOREIGN KEY (physical_link_type_id)
  REFERENCES PhysicalLinkType (id) ON DELETE CASCADE
);

CREATE SEQUENCE physicallink_seq ORDER;
