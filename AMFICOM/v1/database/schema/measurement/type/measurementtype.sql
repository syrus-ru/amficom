-- $Id: measurementtype.sql,v 1.10.2.1 2006/02/14 09:53:54 arseniy Exp $

CREATE TABLE MeasurementType (
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
 CONSTRAINT mt_pk PRIMARY KEY (id),
 CONSTRAINT mt_uniq UNIQUE (codename),
 CONSTRAINT mt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE MeasurementType_seq ORDER;
