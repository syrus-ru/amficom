-- $Id: domain.sql,v 1.3 2004/12/28 13:45:09 arseniy Exp $

CREATE TABLE mcm.domain (
 id VARCHAR2(32),
--
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 owner_id VARCHAR2(32),
--
 CONSTRAINT domain_pk PRIMARY KEY (id),
 CONSTRAINT domain_creator_fk FOREIGN KEY (creator_id)
  REFERENCES mcm.users (id) ON DELETE CASCADE,
 CONSTRAINT domain_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES mcm.users (id) ON DELETE CASCADE,
--
 CONSTRAINT domain_domain_fk FOREIGN KEY (domain_id)
  REFERENCES mcm.domain (id) ON DELETE CASCADE,
--
 CONSTRAINT domain_owner_fk FOREIGN KEY (owner_id)
  REFERENCES mcm.users (id) ON DELETE CASCADE,
 CONSTRAINT domain_name_fk FOREIGN KEY (name) 
  REFERENCES amficom.domains (name) ON DELETE CASCADE
);

CREATE SEQUENCE domain_seq ORDER;

