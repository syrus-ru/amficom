CREATE TABLE MonitoredElement (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 kis_id VARCHAR2(32) NOT NULL,
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
  REFERENCES Equipment (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;
