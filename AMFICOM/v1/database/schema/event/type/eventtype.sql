-- $Id: eventtype.sql,v 1.6 2005/06/15 17:03:09 bass Exp $

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
 CONSTRAINT evtype_pk PRIMARY KEY (id),
 CONSTRAINT evtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT evtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT evtype_uniq UNIQUE (codename)
);

CREATE SEQUENCE EventType_seq ORDER;
