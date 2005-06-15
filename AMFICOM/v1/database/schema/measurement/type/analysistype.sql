-- $Id: analysistype.sql,v 1.7 2005/06/15 07:50:19 bass Exp $

CREATE TABLE AnalysisType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT anatype_pk PRIMARY KEY (id),
 CONSTRAINT anatype_uniq UNIQUE (codename),
 CONSTRAINT anatype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT anatype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE analysistype_seq ORDER;
