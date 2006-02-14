-- $Id: actiontemplate.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE ActionTemplate (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 description VARCHAR2(256 CHAR),
--
 CONSTRAINT actmpl_pk PRIMARY KEY (id),
 CONSTRAINT actmpl_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT actmpl_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE ActionTemplate_seq ORDER;
