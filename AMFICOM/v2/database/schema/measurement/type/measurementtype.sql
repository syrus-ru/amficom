CREATE TABLE MeasurementType (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT mnttype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT mnttype_uniq UNIQUE (codename) ENABLE,
 CONSTRAINT mnttype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT mnttype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE measurementtype_seq ORDER;
