CREATE TABLE ITempPattern (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(32),
--
 CONSTRAINT itp_pk PRIMARY KEY (id),
 CONSTRAINT itp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT itp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);
CREATE SEQUENCE itemppattern_seq ORDER;
