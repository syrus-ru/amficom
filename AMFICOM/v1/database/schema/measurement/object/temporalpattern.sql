CREATE OR REPLACE TYPE CronStrings AS TABLE OF VARCHAR2(64)
/

CREATE TABLE TemporalPattern (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 description VARCHAR2(256),
 value CronStrings,
--
 CONSTRAINT tp_pk PRIMARY KEY (id),
 CONSTRAINT tp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT tp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
 )
 NESTED TABLE value STORE AS value_tab;

CREATE SEQUENCE temporalpattern_seq ORDER;
