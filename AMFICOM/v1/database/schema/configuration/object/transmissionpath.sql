CREATE TABLE TransmissionPath (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
--
 CONSTRAINT tpath_pk PRIMARY KEY (id),
 CONSTRAINT tpath_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT tpath_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT tpath_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE
);

CREATE SEQUENCE transmissionpath_seq ORDER;

