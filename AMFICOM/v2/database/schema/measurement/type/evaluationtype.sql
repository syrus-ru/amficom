CREATE TABLE EvaluationType (
 id NUMBER(20, 0),
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
 CONSTRAINT evatype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT evatype_uniq UNIQUE (codename) ENABLE
);

CREATE SEQUENCE evaluationtype_seq ORDER;
