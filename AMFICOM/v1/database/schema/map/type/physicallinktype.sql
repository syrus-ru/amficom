-- $Id: physicallinktype.sql,v 1.11 2005/08/04 15:22:26 max Exp $

CREATE TABLE PhysicalLinkType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 sort NUMBER(1) NOT NULL,
 codename VARCHAR2(32 CHAR) NOT NULL,
 name VARCHAR2(64 CHAR),
 description VARCHAR2(256 CHAR),
 dimension_x NUMBER(6) NOT NULL,
 dimension_y NUMBER(6) NOT NULL,
 topological NUMBER(1) NOT NULL,
 map_library_id,
--
 CONSTRAINT phlinktype_pk PRIMARY KEY (id),
 CONSTRAINT phlinktype_uniq UNIQUE (codename),
 CONSTRAINT phlinktype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT phlinktype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT phlinktype_maplib_id_fk FOREIGN KEY (map_library_id)
  REFERENCES MapLibrary (id) ON DELETE CASCADE
);

COMMENT ON TABLE PhysicalLinkType IS '$Id: physicallinktype.sql,v 1.11 2005/08/04 15:22:26 max Exp $';

CREATE SEQUENCE PhysicalLinkType_Seq ORDER;
