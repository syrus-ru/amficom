CREATE TABLE TestStatus (
 id NUMBER(2, 0),
 name VARCHAR2(32) NOT NULL,
 comments VARCHAR2(64),
 CONSTRAINT teststatus_pk PRIMARY KEY (id) ENABLE
);
