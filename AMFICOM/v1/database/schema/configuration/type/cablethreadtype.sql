CREATE TABLE CableThreadType (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),
--
 mark VARCHAR2(64),
 color VARCHAR2(32),
 link_type_id VARCHAR2(32),
--
 CONSTRAINT ctkptype_pk PRIMARY KEY (id),
 CONSTRAINT ctptype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT ctptype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE
 CONSTRAINT ctptype_clinktype_fk FOREIGN KEY (link_type_id)
  REFERENCES LinkType (id) ON DELETE CASCADE
);

CREATE SEQUENCE cablethreadtype_seq ORDER;
