-- $Id: measurementtype.sql,v 1.8 2005/06/15 09:40:35 bass Exp $

CREATE TABLE MeasurementType (
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
 CONSTRAINT mnttype_pk PRIMARY KEY (id),
 CONSTRAINT mnttype_uniq UNIQUE (codename),
 CONSTRAINT mnttype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mnttype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementtype_seq ORDER;
