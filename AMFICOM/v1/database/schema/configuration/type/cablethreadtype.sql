-- $Id: cablethreadtype.sql,v 1.16 2006/01/17 16:21:22 bass Exp $

CREATE TABLE CableThreadType (
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
 color NUMBER(10) DEFAULT -1 NOT NULL,
 link_type_id NOT NULL,
 cable_link_type_id NOT NULL,
--
 CONSTRAINT cthtype_pk PRIMARY KEY (id),
 CONSTRAINT cthtype_uniq UNIQUE (codename),
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
