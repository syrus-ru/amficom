CREATE TABLE MonitoredElement (
 id Identifier,
 kis_id Identifier NOT NULL,
 local_address VARCHAR2(64) NOT NULL,
--
 CONSTRAINT me_pk PRIMARY KEY (id),
 CONSTRAINT me_kis_fk FOREIGN KEY (kis_id)
  REFERENCES KIS (id) ON DELETE CASCADE
);

CREATE SEQUENCE monitoredelement_seq ORDER;

