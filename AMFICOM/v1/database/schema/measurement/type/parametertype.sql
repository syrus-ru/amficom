CREATE TABLE ParameterType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT partype_pk PRIMARY KEY (id),
--
 CONSTRAINT partype_uniq UNIQUE (codename),
 CONSTRAINT partype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT partype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
);

CREATE SEQUENCE parametertype_seq ORDER;
