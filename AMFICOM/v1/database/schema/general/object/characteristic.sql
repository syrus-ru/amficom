CREATE TABLE Characteristic (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 type_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 value VARCHAR2(256),
 editable NUMBER(1) NOT NULL,
 visible NUMBER(1) NOT NULL,
--
 sort NUMBER(2) NOT NULL,
 characterized_id VARCHAR2(32),
--
 CONSTRAINT chc_pk PRIMARY KEY (id),
 CONSTRAINT chc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT chc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT chc_chctype_fk FOREIGN KEY (type_id)
  REFERENCES CharacteristicType (id) ON DELETE CASCADE
-- 
);

CREATE SEQUENCE characteristic_seq ORDER;
