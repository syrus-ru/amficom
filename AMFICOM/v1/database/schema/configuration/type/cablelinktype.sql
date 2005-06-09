CREATE TABLE CableLinkType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 name VARCHAR2(128),
 kind NUMBER(1) NOT NULL,
 manufacturer VARCHAR2(128),
 manufacturer_code VARCHAR2(32),
 image_id VARCHAR2(32),
--
 CONSTRAINT cltype_pk PRIMARY KEY (id),
 CONSTRAINT cltype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT cltype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT cltype_kind_chk CHECK (kind >= 0 AND kind <= 2)
);

COMMENT ON COLUMN CableLinkType.kind IS '0 - Optical Fiber, 1 - Ethernet, 2 - GSM';

CREATE SEQUENCE CableLinkType_seq ORDER;
