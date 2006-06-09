-- $Id: server.sql,v 1.11.2.1 2006/06/09 16:40:26 arseniy Exp $

CREATE TABLE Server (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
 hostname VARCHAR2(64 CHAR) NOT NULL,
 system_user_id NOT NULL,
--
 CONSTRAINT server_pk PRIMARY KEY (id),
 CONSTRAINT server_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT server_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT server_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT server_user_fk FOREIGN KEY (system_user_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE 
);

CREATE SEQUENCE server_seq ORDER;

