CREATE TABLE LinkType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 name VARCHAR2(64),
--
 sort NUMBER(2,0),
 manufacturer VARCHAR2(64),
 manufacturer_code VARCHAR2(64),
 image_id VARCHAR2(32),
--
 CONSTRAINT lkptype_pk PRIMARY KEY (id),
 CONSTRAINT lkptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE linktype_seq ORDER;
