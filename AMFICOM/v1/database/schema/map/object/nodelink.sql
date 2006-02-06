-- $Id: nodelink.sql,v 1.8 2005/08/26 09:37:03 max Exp $

CREATE TABLE NodeLink (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 physical_link_id,
 start_node_id NUMBER(19),
 end_node_id NUMBER(19), 
 length NUMBER(12, 6),
--
 CONSTRAINT nodelink_pk PRIMARY KEY (id),
 CONSTRAINT nodelink_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT nodelink_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT nodelink_phlink_type_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE NodeLink IS '$Id: nodelink.sql,v 1.8 2005/08/26 09:37:03 max Exp $';

CREATE SEQUENCE NodeLink_Seq ORDER;
