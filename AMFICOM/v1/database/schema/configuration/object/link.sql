-- $Id: link.sql,v 1.14 2005/06/15 17:03:09 bass Exp $

CREATE TABLE Link (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id,
--
 type_id NOT NULL,
--
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
--
 inventory_no VARCHAR2(64 CHAR),
 supplier VARCHAR2(128 CHAR),
 supplier_code VARCHAR2(32 CHAR),
 color VARCHAR(32),
 mark VARCHAR(32),
--
 CONSTRAINT link_pk PRIMARY KEY (id),
 CONSTRAINT link_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT link_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT link_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT link_lnktype_fk FOREIGN KEY (type_id)
  REFERENCES LinkType (id) ON DELETE CASCADE
);

CREATE SEQUENCE Link_Seq ORDER;
