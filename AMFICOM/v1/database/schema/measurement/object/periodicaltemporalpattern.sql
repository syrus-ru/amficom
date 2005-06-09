CREATE TABLE PeriodicalTemporalPattern (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 period NUMBER(19) NOT NULL,
--
 CONSTRAINT ptp_pk PRIMARY KEY (id),
 CONSTRAINT ptp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT ptp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE periodicaltemporalpattern_seq ORDER;
