CREATE TABLE Sett (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
--
 sort NUMBER(2, 0) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT sett_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT sett_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT sett_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
--
 CONSTRAINT sett_setsort_fk FOREIGN KEY (sort)
  REFERENCES SetSort(id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE sett_seq ORDER;
