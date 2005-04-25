CREATE OR REPLACE TYPE CronStringArray AS TABLE OF VARCHAR2(64)
/

CREATE TABLE CronTemporalPattern (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 description VARCHAR2(256),
 value CronStringArray,
--
 CONSTRAINT ctp_pk PRIMARY KEY (id),
 CONSTRAINT ctp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT ctp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
 )
 NESTED TABLE value STORE AS cronvaluetab;

CREATE SEQUENCE crontemporalpattern_seq ORDER;
