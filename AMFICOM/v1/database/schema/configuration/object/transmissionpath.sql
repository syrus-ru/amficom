CREATE TABLE TransmissionPath (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 domain_id Identifier,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT tp_pk PRIMARY KEY (id),
 CONSTRAINT tp_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT tp_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT tp_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
);

CREATE SEQUENCE transmissionpath_seq ORDER;

