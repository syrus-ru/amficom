-- $Id: cablethreadtype.sql,v 1.12 2005/06/15 09:40:34 bass Exp $

CREATE TABLE CableThreadType (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 codename VARCHAR2(32 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 name VARCHAR2(128 CHAR),
 mark VARCHAR2(64 CHAR),
 color VARCHAR2(32 CHAR),
 link_type_id NUMBER(19),
 cable_link_type_id NUMBER(19),
--
 CONSTRAINT cthtype_pk PRIMARY KEY (id),
 CONSTRAINT cthtype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT cthtype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT cthtype_linktype_fk FOREIGN KEY (link_type_id)
  REFERENCES LinkType (id) ON DELETE CASCADE,
 CONSTRAINT cthtype_clinktype_fk FOREIGN KEY (cable_link_type_id)
  REFERENCES CableLinkType (id) ON DELETE CASCADE
);

CREATE SEQUENCE CableThreadType_Seq ORDER;
