-- $Id: cablelinktype.sql,v 1.2 2005/06/15 07:50:18 bass Exp $

CREATE TABLE CableLinkType (
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
 kind NUMBER(1) NOT NULL,
 manufacturer VARCHAR2(128),
 manufacturer_code VARCHAR2(32),
 image_id NUMBER(19),
--
 CONSTRAINT cltype_pk PRIMARY KEY (id),
 CONSTRAINT cltype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT cltype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT cltype_image_fk FOREIGN KEY (image_id)
  REFERENCES ImageResource (id) ON DELETE CASCADE,
 CONSTRAINT cltype_kind_chk CHECK (
  kind >= 0 AND kind <= 2)
);

COMMENT ON COLUMN CableLinkType.kind IS '0 stands for OpticalFiber, 1 for Ethernet, 2 for Gsm.';

CREATE SEQUENCE CableLinkType_Seq ORDER;
