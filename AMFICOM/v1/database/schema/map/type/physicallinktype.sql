CREATE TABLE PhysicalLinkType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 name VARCHAR2(128),
 description VARCHAR2(256),
 dimension_x NUMBER(6),
 dimension_y NUMBER(6),
--
 CONSTRAINT phlinktype_pk PRIMARY KEY (id),
 CONSTRAINT phlinktype_uniq UNIQUE (codename),
 CONSTRAINT phlinktype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT phlinktype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE physycallinktype_seq ORDER;
