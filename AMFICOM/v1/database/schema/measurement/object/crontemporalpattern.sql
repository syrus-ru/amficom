-- $Id: crontemporalpattern.sql,v 1.5 2005/06/15 17:03:09 bass Exp $

CREATE OR REPLACE TYPE CronStringArray AS TABLE OF VARCHAR2(64 CHAR)
/

CREATE TABLE CronTemporalPattern (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 description VARCHAR2(256 CHAR),
 value CronStringArray,
--
 CONSTRAINT ctp_pk PRIMARY KEY (id),
 CONSTRAINT ctp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ctp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
 )
 NESTED TABLE value STORE AS cronvaluetab;

CREATE SEQUENCE crontemporalpattern_seq ORDER;
