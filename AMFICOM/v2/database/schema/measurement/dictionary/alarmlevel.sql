CREATE TABLE AlarmLevel (
 id NUMBER(2, 0), 
 name VARCHAR2(32) NOT NULL, 
 comments VARCHAR2(64), 
 CONSTRAINT al_pk PRIMARY KEY (id) ENABLE
);