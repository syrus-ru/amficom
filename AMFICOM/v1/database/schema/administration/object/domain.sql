-- $Id: domain.sql,v 1.9 2005/06/09 14:40:10 max Exp $

CREATE TABLE Domain (
 id NUMBER(19),
--
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 owner_id NUMBER(19),
--
 CONSTRAINT domain_pk PRIMARY KEY (id),
 CONSTRAINT domain_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystmeUser (id) ON DELETE CASCADE,
 CONSTRAINT domain_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT domain_domain_fk FOREIGN KEY (domain_id)
  REFERENCES domain (id) ON DELETE CASCADE,
--
 CONSTRAINT domain_owner_fk FOREIGN KEY (owner_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE Domain_seq ORDER;

