CREATE TABLE ModelingType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT modtype_pk PRIMARY KEY (id),
 CONSTRAINT modtype_uniq UNIQUE (codename),
 CONSTRAINT modtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT modtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE ModelingType_seq ORDER;
