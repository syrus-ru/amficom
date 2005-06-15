-- $Id: server.sql,v 1.8 2005/06/15 07:50:17 bass Exp $

CREATE TABLE Server (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 type_id NUMBER(19),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 hostname VARCHAR2(64) NOT NULL,
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
 CONSTRAINT server_servertype_fk FOREIGN KEY (type_id)
  REFERENCES ServerType (id) ON DELETE CASCADE
);

CREATE SEQUENCE server_seq ORDER;

