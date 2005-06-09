-- $Id: physicallink.sql,v 1.5 2005/06/09 14:40:11 max Exp $

CREATE TABLE PhysicalLink (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 physical_link_type_id NUMBER(19) NOT NULL,
 city VARCHAR2(128),
 street VARCHAR2(128),
 building VARCHAR2(128),
 dimension_x NUMBER(12),
 dimension_y NUMBER(12),
 topLeft NUMBER(1),
 start_node_id NUMBER(19),
 end_node_id NUMBER(19),
--
 CONSTRAINT phlink_pk PRIMARY KEY (id),
 CONSTRAINT phlink_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT phlink_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT phlink_type_modifier_fk FOREIGN KEY (physical_link_type_id)
  REFERENCES PhysicalLinkType (id) ON DELETE CASCADE
);

COMMENT ON TABLE PhysicalLink IS '$Id: physicallink.sql,v 1.5 2005/06/09 14:40:11 max Exp $';

CREATE SEQUENCE PhysicalLink_Seq ORDER;
