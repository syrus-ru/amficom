CREATE TABLE MeasurementPortType (
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
 name VARCHAR2(128),
--
 CONSTRAINT mptype_pk PRIMARY KEY (id),
 CONSTRAINT mptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT mptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES systemUser (id) ON DELETE CASCADE
);

CREATE SEQUENCE MeasurementPortType_seq ORDER;

