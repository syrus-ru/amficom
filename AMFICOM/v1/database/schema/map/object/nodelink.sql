-- $Id: nodelink.sql,v 1.6 2005/06/15 09:40:34 bass Exp $

CREATE TABLE NodeLink (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR),
 physical_link_id NUMBER(19),
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

COMMENT ON TABLE NodeLink IS '$Id: nodelink.sql,v 1.6 2005/06/15 09:40:34 bass Exp $';

CREATE SEQUENCE NodeLink_Seq ORDER;
