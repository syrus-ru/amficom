CREATE OR REPLACE TYPE CronStringArray AS TABLE OF VARCHAR2(64)
/

CREATE TABLE TemporalPattern (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 description VARCHAR2(256),
 value CronStringArray,
--
 CONSTRAINT tp_pk PRIMARY KEY (id),
 CONSTRAINT tp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT tp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
 )
 NESTED TABLE value STORE AS value_tab;

CREATE SEQUENCE temporalpattern_seq ORDER;
