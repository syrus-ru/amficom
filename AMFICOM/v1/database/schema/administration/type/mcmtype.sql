CREATE TABLE MCMType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT mcmtype_pk PRIMARY KEY (id),
 CONSTRAINT mcmtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mcmtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystmeUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcmtype_seq ORDER;
