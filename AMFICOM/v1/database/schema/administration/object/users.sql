CREATE TABLE Users(
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 login VARCHAR2(32) NOT NULL,
 sort NUMBER(2, 0) NOT NULL,
 last_logged DATE NOT NULL,
 logged DATE NOT NULL,
 sessions NUMBER(10, 0),
--
 CONSTRAINT users_pk PRIMARY KEY (id),
 CONSTRAINT users_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT users_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE users_seq ORDER;
