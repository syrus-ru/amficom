CREATE TABLE Sett (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 sort NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT sett_pk PRIMARY KEY (id),
 CONSTRAINT sett_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT sett_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE sett_seq ORDER;
