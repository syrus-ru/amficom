CREATE TABLE KIS (
 id Identifier,
 mcm_id Identifier NOT NULL,
--
 CONSTRAINT kis_pk PRIMARY KEY (id),
 CONSTRAINT kis_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES MCM (id) ON DELETE CASCADE
);

CREATE SEQUENCE kis_seq ORDER;

