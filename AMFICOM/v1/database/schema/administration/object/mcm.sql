CREATE TABLE MCM (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 type_id VARCHAR2(32),
--
 name VARCHAR2(128) NOT NULL,
 description VARCHAR2(256),
 hostname VARCHAR2(64) NOT NULL,
 user_id VARCHAR2(32) NOT NULL,
 server_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT mcm_pk PRIMARY KEY (id),
 CONSTRAINT mcm_creator_fk FOREIGN KEY (creator_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mcm_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_mcmtype_fk FOREIGN KEY (type_id)
  REFERENCES MCMType (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_user_fk FOREIGN KEY (user_id)
  REFERENCES "User" (id) ON DELETE CASCADE,
 CONSTRAINT mcm_server_fk FOREIGN KEY (server_id)
  REFERENCES Server (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcm_seq ORDER;
