CREATE TABLE AnalysisType (
 id NUMBER(20, 0),
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
 CONSTRAINT anatype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT anatype_uniq UNIQUE (codename) ENABLE
);

CREATE SEQUENCE analysistype_seq ORDER;
