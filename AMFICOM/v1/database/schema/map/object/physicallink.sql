-- $Id: physicallink.sql,v 1.9 2005/10/19 14:40:31 max Exp $

CREATE TABLE PhysicalLink (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 physical_link_type_id NOT NULL,
 city VARCHAR2(128 CHAR),
 street VARCHAR2(128 CHAR),
 building VARCHAR2(128 CHAR),
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

COMMENT ON TABLE PhysicalLink IS '$Id: physicallink.sql,v 1.9 2005/10/19 14:40:31 max Exp $';

CREATE SEQUENCE PhysicalLink_Seq ORDER;
