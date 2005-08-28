-- $Id: measurementporttype.sql,v 1.2 2005/08/28 14:29:24 arseniy Exp $

CREATE TABLE MeasurementPortType (
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
 CONSTRAINT mptyp_pk PRIMARY KEY (id),
 CONSTRAINT mptyp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mptyp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE MeasurementPortType_seq ORDER;

