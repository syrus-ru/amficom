CREATE TABLE ParameterType (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT parametertype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT parametertype_uniq UNIQUE (codename) ENABLE,
 CONSTRAINT parametertype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT parametertype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE parametertype_seq ORDER;
