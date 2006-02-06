-- $Id: mcmtype.sql,v 1.8 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE MCMType (
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
 CONSTRAINT mcmtype_pk PRIMARY KEY (id),
 CONSTRAINT mcmtype_uniq UNIQUE (codename),
 CONSTRAINT mcmtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mcmtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcmtype_seq ORDER;
