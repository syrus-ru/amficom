-- $Id: mark.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Mark (
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
 physical_link_id NOT NULL,
 distance NUMBER(12, 6),
 city VARCHAR2(128 CHAR),
 street VARCHAR2(128 CHAR),
 building VARCHAR2(128 CHAR),
--
 CONSTRAINT mark_pk PRIMARY KEY (id),
 CONSTRAINT mark_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mark_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mark_phlink_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE Mark IS '$Id: mark.sql,v 1.7 2005/06/15 17:03:09 bass Exp $';

CREATE SEQUENCE Mark_Seq ORDER;
