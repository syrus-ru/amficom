CREATE TABLE TestTemporalType (
 id NUMBER(2, 0),
 name VARCHAR2(32) NOT NULL,
 comments VARCHAR2(64),
 CONSTRAINT ttt_pk PRIMARY KEY (id) ENABLE
);
