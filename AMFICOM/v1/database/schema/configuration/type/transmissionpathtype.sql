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
 CONSTRAINT tpathtype_pk PRIMARY KEY (id),
 CONSTRAINT tpathtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT tpathtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE SEQUENCE transmissionpathtype_seq ORDER;
