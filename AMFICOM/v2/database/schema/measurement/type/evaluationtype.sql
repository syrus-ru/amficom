CREATE TABLE EvaluationType (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),

 CONSTRAINT evatype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT evatype_uniq UNIQUE (codename) ENABLE,
 CONSTRAINT evatype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT evatype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE evaluationtype_seq ORDER;
