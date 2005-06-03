-- $Id: mark.sql,v 1.4 2005/06/03 11:48:02 bass Exp $

CREATE TABLE Mark (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 longitude NUMBER(12, 6),
 latiude NUMBER(12, 6),
 physical_link_id VARCHAR2(32) NOT NULL,
 distance NUMBER(12, 6),
 city VARCHAR2(128),
 street VARCHAR2(128),
 building VARCHAR2(128),
--
 CONSTRAINT mark_pk PRIMARY KEY (id),
 CONSTRAINT mark_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mark_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mark_phlink_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE
);

COMMENT ON TABLE Mark IS '$Id: mark.sql,v 1.4 2005/06/03 11:48:02 bass Exp $';

CREATE SEQUENCE Mark_Seq ORDER;
