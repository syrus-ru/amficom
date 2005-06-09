-- $Id: map.sql,v 1.5 2005/06/09 14:40:11 max Exp $

CREATE TABLE Map (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 domain_id NUMBER(19),
--
 CONSTRAINT map_pk PRIMARY KEY (id),
 CONSTRAINT map_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT map_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT map_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

COMMENT ON TABLE Map IS '$Id: map.sql,v 1.5 2005/06/09 14:40:11 max Exp $';

CREATE SEQUENCE Map_Seq ORDER;
