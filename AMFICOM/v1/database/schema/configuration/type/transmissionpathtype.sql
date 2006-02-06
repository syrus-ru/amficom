-- $Id: transmissionpathtype.sql,v 1.9 2005/09/28 10:34:02 arseniy Exp $

CREATE TABLE TransmissionPathType (
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
 --
 CONSTRAINT tpathtype_pk PRIMARY KEY (id),
 CONSTRAINT tpathtype_uniq UNIQUE (codename),
 CONSTRAINT tpathtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT tpathtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE transmissionpathtype_seq ORDER;
