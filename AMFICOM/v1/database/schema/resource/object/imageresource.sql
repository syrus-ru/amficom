CREATE TABLE ImageResource (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 sort NUMBER(2),
 image BLOB,
--
 CONSTRAINT imres_pk PRIMARY KEY (id),
 CONSTRAINT imres_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT imres_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT imres_uniq UNIQUE (codename)
);

CREATE SEQUENCE ImageResource_seq ORDER;
