CREATE TABLE Link (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(19) NOT NULL,
 modifier_id NUMBER(19) NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NUMBER(19),
--
 type_id NUMBER(19) NOT NULL,
 nature NUMBER(1) NOT NULL,
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 inventory_no VARCHAR2(64),
 supplier VARCHAR2(128),
 supplier_code VARCHAR2(32),
 link_id NUMBER(19),
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
  REFERENCES LinkType (id) ON DELETE CASCADE,
--
 CONSTRAINT link_nature_chk CHECK (
  nature >= 0 AND nature <= 1)
);

COMMENT ON COLUMN Link.nature IS '0 stands for Link, 1 for CableLink';

CREATE SEQUENCE link_seq ORDER;
