CREATE TABLE TransmissionPathType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 --
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
 --
 name VARCHAR2(128),
 --
 CONSTRAINT kistype_pk PRIMARY KEY (id),
 CONSTRAINT kistype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT kistype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE transmissionpathtype_seq ORDER;
