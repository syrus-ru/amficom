CREATE TABLE MeasurementType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT mnttype_pk PRIMARY KEY (id),
 CONSTRAINT mnttype_uniq UNIQUE (codename),
 CONSTRAINT mnttype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mnttype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE
);

CREATE SEQUENCE measurementtype_seq ORDER;
