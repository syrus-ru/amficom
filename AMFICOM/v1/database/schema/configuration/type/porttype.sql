-- $Id: porttype.sql,v 1.13 2005/06/28 08:30:21 arseniy Exp $

CREATE TABLE PortType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 name VARCHAR2(128 CHAR),
 sort NUMBER(2,0) NOT NULL,
 kind NUMBER(2,0) NOT NULL,
--
 CONSTRAINT porttype_pk PRIMARY KEY (id),
 CONSTRAINT porttype_uniq UNIQUE (codename),
 CONSTRAINT porttype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT porttype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE porttype_seq ORDER;
