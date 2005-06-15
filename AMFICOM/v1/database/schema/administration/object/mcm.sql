-- $Id: mcm.sql,v 1.9 2005/06/15 17:03:09 bass Exp $

CREATE TABLE MCM (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id,
--
 type_id,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
 hostname VARCHAR2(64 CHAR) NOT NULL,
 user_id NOT NULL,
 server_id NOT NULL,
--
 CONSTRAINT mcm_pk PRIMARY KEY (id),
 CONSTRAINT mcm_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mcm_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_mcmtype_fk FOREIGN KEY (type_id)
  REFERENCES MCMType (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_user_fk FOREIGN KEY (user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mcm_server_fk FOREIGN KEY (server_id)
  REFERENCES Server (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcm_seq ORDER;
