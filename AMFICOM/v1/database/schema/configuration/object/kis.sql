CREATE TABLE KIS (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 hostname VARCHAR2(64),
 tcp_port NUMBER(5,0),
 equipment_id varchar2(32) NOT NULL,
 mcm_id VARCHAR2(32) NOT NULL,
--
 CONSTRAINT kis_pk PRIMARY KEY (id),
 CONSTRAINT kis_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT kis_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
 CONSTRAINT kis_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT kis_eqp_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE CASCADE,
 CONSTRAINT kis_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES MCM (id) ON DELETE CASCADE
);

CREATE SEQUENCE kis_seq ORDER;

