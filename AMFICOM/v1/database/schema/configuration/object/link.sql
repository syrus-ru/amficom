CREATE TABLE Equipment (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 type_id VARCHAR2(32) NOT NULL,
 sort NUMBER(2,0),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
--
 inventory_no VARCHAR2(64),
 supplier VARCHAR2(128),
 supplier_code VARCHAR2(32),
 link_id VARCHAR2(32),
 color VARCHAR(32),
 mark VARCHAR(32),
--
 CONSTRAINT linkp_pk PRIMARY KEY (id),
 CONSTRAINT linkp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT linkp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT linkp_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT linkp_lnktype_fk FOREIGN KEY (type_id)
  REFERENCES LinkType (id) ON DELETE CASCADE
);

CREATE SEQUENCE link_seq ORDER;

