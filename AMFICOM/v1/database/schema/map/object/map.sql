CREATE TABLE Map (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 name VARCHAR2(128),
 description VARCHAR2(256),
 domain_id VARCHAR2(32),
--
 CONSTRAINT map_pk PRIMARY KEY (id),
 CONSTRAINT map_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT map_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT map_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

CREATE SEQUENCE map_seq ORDER;
