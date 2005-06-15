-- $Id: eventtype.sql,v 1.5 2005/06/15 09:40:34 bass Exp $

CREATE TABLE EventType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
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
