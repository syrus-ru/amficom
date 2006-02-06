-- $Id: parameterset.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE ParameterSet (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 sort NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT paramset_pk PRIMARY KEY (id),
 CONSTRAINT paramset_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT paramset_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE parameterset_seq ORDER;
