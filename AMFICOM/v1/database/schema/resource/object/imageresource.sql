CREATE TABLE imageresource (
 id VARCHAR2(32);
--
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2 NOT NULL,
 modifier_id VARCHAR2 NOT NULL,
--
 codename VARCHAR2(256),
 image BLOB,
 sort NUMBER(1) NOT NULL,
--
 CONSTRAINT imgres_pk PRIMARY KEY (id),
 CONSTRAINT imgres_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT imgres_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT imgres_uniq UNIQUE (CODENAME)
);
 

 CREATE SEQUENCE ImageResource_seq ORDER;
