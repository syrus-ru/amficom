CREATE TABLE ServerType (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT servertype_pk PRIMARY KEY (id),
 CONSTRAINT servertype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT servertype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE servertype_seq ORDER;
