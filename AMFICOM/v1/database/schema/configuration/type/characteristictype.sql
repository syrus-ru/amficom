CREATE TABLE CharacteristicType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 is_editable NUMBER(1) NOT NULL,
 is_visible NUMBER(1) NOT NULL,
 data_type NUMBER(2) NOT NULL,
--
 CONSTRAINT chctype_pk PRIMARY KEY (id),
 CONSTRAINT chctype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT chctype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT chctype_dt_fk FOREIGN KEY (data_type)
  REFERENCES DataType (id) ON DELETE CASCADE
);

CREATE SEQUENCE characteristictype_seq ORDER;

