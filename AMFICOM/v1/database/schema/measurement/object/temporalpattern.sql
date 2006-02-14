-- $Id: temporalpattern.sql,v 1.7.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE TemporalPattern (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 period NUMBER(19) NOT NULL,
--
 CONSTRAINT tp_pk PRIMARY KEY (id),
 CONSTRAINT tp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT tp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE TemporalPattern_seq ORDER;
