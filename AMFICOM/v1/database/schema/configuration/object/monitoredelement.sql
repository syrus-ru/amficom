CREATE TABLE MonitoredElement (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 domain_id Identifier,
--
 kis_id Identifier NOT NULL,
 local_address VARCHAR2(64) NOT NULL,
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT me_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT me_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT me_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;

