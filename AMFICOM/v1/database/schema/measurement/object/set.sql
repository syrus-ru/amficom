CREATE TABLE "Set" (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 sort NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT set_pk PRIMARY KEY (id),
 CONSTRAINT set_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT set_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE SEQUENCE set_seq ORDER;
