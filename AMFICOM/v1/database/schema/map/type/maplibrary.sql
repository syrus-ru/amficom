-- $Id: maplibrary.sql,v 1.4.2.1 2006/02/14 11:07:17 arseniy Exp $

CREATE TABLE MapLibrary (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(64 CHAR),
 codename VARCHAR2(32 CHAR),
 description VARCHAR2(256 CHAR),
 parent_map_library_id,
--
 CONSTRAINT maplib_pk PRIMARY KEY (id),
 CONSTRAINT maplib_uniq UNIQUE (codename),
 CONSTRAINT maplib_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT maplib_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT maplib_parmaplib_fk FOREIGN KEY (parent_map_library_id)
  REFERENCES MapLibrary (id) ON DELETE CASCADE
);
CREATE SEQUENCE MapLibrary_Seq;
