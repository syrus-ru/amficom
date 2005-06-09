CREATE TABLE LinkType (
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
 nature NUMBER(1) NOT NULL,
 kind NUMBER(1) NOT NULL,
 manufacturer VARCHAR2(128),
 manufacturer_code VARCHAR2(32),
 image_id VARCHAR2(32),
--
 CONSTRAINT lkptype_pk PRIMARY KEY (id),
 CONSTRAINT lkptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_nature_chk CHECK (
  nature >= 0 AND nature <= 1),
 CONSTRAINT lkptype_kind_chk CHECK (
  kind >= 0 AND kind <= 2)
);

COMMENT ON COLUMN LinkType.nature IS '0 stands for LinkType, 1 for CableLinktype.';
COMMENT ON COLUMN LinkType.kind IS '0 stands for OpticalFiber, 1 for Ethernet, 2 for Gsm.';

CREATE SEQUENCE LinkType_seq ORDER;
