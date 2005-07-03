-- $Id: map.sql,v 1.7 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Map (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 domain_id,
--
 CONSTRAINT map_pk PRIMARY KEY (id),
 CONSTRAINT map_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT map_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT map_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

COMMENT ON TABLE Map IS '$Id: map.sql,v 1.7 2005/06/15 17:03:09 bass Exp $';

CREATE SEQUENCE Map_Seq ORDER;
