-- $Id: eventtype.sql,v 1.6.2.1 2006/02/14 11:09:36 arseniy Exp $

CREATE TABLE EventType (
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
 CONSTRAINT et_pk PRIMARY KEY (id),
 CONSTRAINT et_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT et_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT et_uniq UNIQUE (codename)
);

CREATE SEQUENCE EventType_seq ORDER;
