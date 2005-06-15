-- $Id: evaluationtype.sql,v 1.8 2005/06/15 09:40:35 bass Exp $

CREATE TABLE EvaluationType (
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
 CONSTRAINT evatype_pk PRIMARY KEY (id),
 CONSTRAINT evatype_uniq UNIQUE (codename),
 CONSTRAINT evatype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT evatype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE evaluationtype_seq ORDER;
