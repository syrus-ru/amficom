CREATE TABLE PeriodicalTemporalPattern (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 period NUMBER(19) NOT NULL,
--
 CONSTRAINT ptp_pk PRIMARY KEY (id),
 CONSTRAINT ptp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT ptp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE SEQUENCE periodicaltemporalpattern_seq ORDER;
