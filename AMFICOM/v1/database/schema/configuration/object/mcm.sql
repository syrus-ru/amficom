CREATE TABLE MCM (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 domain_id Identifier,
--
 type_id Identifier,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 user_id Identifier NOT NULL,
 server_id Identifier NOT NULL,
--
 CONSTRAINT mcm_pk PRIMARY KEY (id),
 CONSTRAINT mcm_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mcm_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_mcmtype_fk FOREIGN KEY (type_id)
  REFERENCES MCMType (id) ON DELETE CASCADE,
--
 CONSTRAINT mcm_user_fk FOREIGN KEY (user_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT mcm_server_fk FOREIGN KEY (server_id)
  REFERENCES Server (id) ON DELETE CASCADE
);

CREATE SEQUENCE mcm_seq ORDER;
