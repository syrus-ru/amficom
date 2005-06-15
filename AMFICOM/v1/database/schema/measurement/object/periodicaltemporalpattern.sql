-- $Id: periodicaltemporalpattern.sql,v 1.4 2005/06/15 17:03:09 bass Exp $

CREATE TABLE PeriodicalTemporalPattern (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
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
