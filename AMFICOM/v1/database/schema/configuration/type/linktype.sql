-- $Id: linktype.sql,v 1.13 2005/06/15 17:03:09 bass Exp $

CREATE TABLE LinkType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 name VARCHAR2(128 CHAR),
 kind NUMBER(1) NOT NULL,
 manufacturer VARCHAR2(128 CHAR),
 manufacturer_code VARCHAR2(32 CHAR),
 image_id,
--
 CONSTRAINT lkptype_pk PRIMARY KEY (id),
 CONSTRAINT lkptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE,
 CONSTRAINT lkptype_kind_chk CHECK (
  kind >= 0 AND kind <= 2)
);

COMMENT ON COLUMN LinkType.kind IS '0 stands for OpticalFiber, 1 for Ethernet, 2 for Gsm.';

CREATE SEQUENCE LinkType_Seq ORDER;
