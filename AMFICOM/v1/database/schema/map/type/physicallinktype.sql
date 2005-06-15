-- $Id: physicallinktype.sql,v 1.7 2005/06/15 09:40:34 bass Exp $

CREATE TABLE PhysicalLinkType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(128 CHAR),
 description VARCHAR2(256 CHAR),
 dimension_x NUMBER(6),
 dimension_y NUMBER(6),
--
 CONSTRAINT phlinktype_pk PRIMARY KEY (id),
 CONSTRAINT phlinktype_uniq UNIQUE (codename),
 CONSTRAINT phlinktype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT phlinktype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

COMMENT ON TABLE PhysicalLinkType IS '$Id: physicallinktype.sql,v 1.7 2005/06/15 09:40:34 bass Exp $';

CREATE SEQUENCE PhysicalLinkType_Seq ORDER;
