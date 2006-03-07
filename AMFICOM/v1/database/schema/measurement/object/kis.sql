-- $Id: kis.sql,v 1.2.2.2 2006/03/07 10:38:26 arseniy Exp $

CREATE TABLE KIS (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NOT NULL,
 name VARCHAR2(128 CHAR) NOT NULL,
 description VARCHAR2(256 CHAR),
 hostname VARCHAR2(64 CHAR) NOT NULL,
 tcp_port NUMBER(5,0) NOT NULL,
 equipment_id NOT NULL,
 mcm_id NOT NULL,
 on_service NUMBER(1) NOT NULL,
--
 CONSTRAINT kis_pk PRIMARY KEY (id),
 CONSTRAINT kis_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT kis_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT kis_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE,
--
 CONSTRAINT kis_eqp_fk FOREIGN KEY (equipment_id)
  REFERENCES Equipment (id) ON DELETE SET NULL,
 CONSTRAINT kis_mcm_fk FOREIGN KEY (mcm_id)
  REFERENCES MCM (id) ON DELETE CASCADE,
 CONSTRAINT kis_os_check CHECK (on_service = 0 OR on_service = 1)
);

CREATE SEQUENCE kis_seq ORDER;
