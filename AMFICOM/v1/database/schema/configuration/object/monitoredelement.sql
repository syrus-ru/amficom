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
--
 sort NUMBER(2) NOT NULL,
 local_address VARCHAR2(64) NOT NULL,
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;

