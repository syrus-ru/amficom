CREATE TABLE Event (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT event_pk PRIMARY KEY (id),
 CONSTRAINT event_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT event_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT event_evtype_fk FOREIGN KEY (type_id)
  REFERENCES EventType (id) ON DELETE CASCADE
);

CREATE SEQUENCE Event_seq ORDER;
