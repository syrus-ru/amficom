CREATE TABLE MeasurementType (
 id NUMBER(20, 0),
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
 CONSTRAINT mnttype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT mnttype_uniq UNIQUE (codename) ENABLE
);

CREATE SEQUENCE measurementtype_seq ORDER;
