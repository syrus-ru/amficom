CREATE TABLE ParameterType (
 id NUMBER(20, 0),
 codename VARCHAR2(32) NOT NULL,
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 CONSTRAINT parametertype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT parametertype_uniq UNIQUE (codename) ENABLE
);

CREATE SEQUENCE parametertype_seq ORDER;
